package net.roshambo.test;

import lombok.extern.slf4j.Slf4j;
import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.RoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        webEnvironment = RANDOM_PORT
)
@Slf4j
@DisplayName("INTEGRATION TESTS FOR API")
@Testcontainers
public class IntegrationTests {
    @LocalServerPort
    private int port;
    private WebTestClient client;

    @Autowired
    private RoundRepository roundRepository;

    @ServiceConnection
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.flyway.url", postgresContainer::getJdbcUrl);
        registry.add("spring.flyway.password", postgresContainer::getPassword);
        registry.add("spring.flyway.username", postgresContainer::getUsername);

        registry.add("spring.r2dbc.url", () ->
                String.format("r2dbc:pool:postgresql://%s:%d/%s",
                        postgresContainer.getHost(),
                        postgresContainer.getFirstMappedPort(),
                        postgresContainer.getDatabaseName()));
        registry.add("spring.r2dbc.username", postgresContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        this.client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port).build();
    }

    @Nested
    class MoveTests {

        @BeforeEach
        void setup() {
            roundRepository.deleteAll().block();
        }

        @Test
        @DisplayName("should return ok when player A makes a valid move")
        void shouldReturnOkWhenPlayerAMakesMove() {
            client.post().uri(builder ->
                            builder.path(ServiceURI.MAKE_MOVE)
                                    .queryParam("player", Player.A)
                                    .queryParam("move", Move.ROCK)
                                    .build())
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @DisplayName("should return bad request when player makes same move second time")
        void shouldReturn400WhenPlayerMakesMoveSecondTime() {
            shouldReturnOkWhenPlayerAMakesMove();
            client.post().uri(builder ->
                            builder.path(ServiceURI.MAKE_MOVE)
                                    .queryParam("player", Player.A)
                                    .queryParam("move", Move.ROCK)
                                    .build())
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("should return bad request when player makes illegal move")
        void shouldReturn400WhenPlayerMakesIllegalMove() {
            client.post().uri(builder ->
                            builder.path(ServiceURI.MAKE_MOVE)
                                    .queryParam("player", Player.A)
                                    .queryParam("move", Move.NONE)
                                    .build())
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("should return ok, when 2 players make moves")
        void shouldReturnOkWhen2PlayersMakeValidMoves() {
            client.post().uri(builder ->
                            builder.path(ServiceURI.MAKE_MOVE)
                                    .queryParam("player", Player.A)
                                    .queryParam("move", Move.ROCK)
                                    .build())
                    .exchange()
                    .expectStatus().isOk();
            client.post().uri(builder ->
                            builder.path(ServiceURI.MAKE_MOVE)
                                    .queryParam("player", Player.B)
                                    .queryParam("move", Move.SCISSORS)
                                    .build())
                    .exchange()
                    .expectStatus().isOk();
        }
    }

    @Nested
    class HistoryTests {

        @BeforeEach
        void setup() {
            roundRepository.deleteAll().block();
            roundRepository.save(
                    Round.builder()
                    .moveA(Move.ROCK)
                    .moveB(Move.SCISSORS)
                    .build()
            ).block();
            roundRepository.save(
                    Round.builder()
                    .moveA(Move.PAPER)
                    .moveB(Move.SCISSORS)
                    .build()
            ).block();
        }

        @Test
        @DisplayName("should return ok when user watch history")
        void shouldReturnOkWhenUserWatchHistory() {
            client.get().uri(builder ->
                            builder.path(ServiceURI.HISTORY)
                                    .queryParam("size", 10)
                                    .queryParam("page", 0)
                                    .queryParam("orderBy", "DESC")
                                    .queryParam("sortBy", "createdAt")
                                    .build())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.content").isArray()
                    .jsonPath("$.content.length()").isEqualTo(2)
                    .jsonPath("$.size").isEqualTo(10)
                    .jsonPath("$.number").isEqualTo(0);
        }

        @Test
        @DisplayName("should return bad request when user watch history wih illegal params")
        void shouldReturn400WhenUserWatchHistoryWithIllegalParams() {
            client.get().uri(builder ->
                            builder.path(ServiceURI.HISTORY)
                                    .queryParam("size", -10)
                                    .queryParam("page", -10)
                                    .queryParam("orderBy", "null")
                                    .queryParam("sortBy", "null")
                                    .build())
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        @DisplayName("should return ok with statistic")
        void shouldReturnOkWithStatistic() {
            client.get().uri(ServiceURI.STATISTIC)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.playerAWins").exists()
                    .jsonPath("$.playerBWins").exists()
                    .jsonPath("$.tie").exists()
                    .jsonPath("$.total").exists();
        }

        @Test
        @DisplayName("should return ok when user clears history")
        void shouldReturnOkWhenUserClearsHistory() {
            client.delete().uri(ServiceURI.CLEAR_HISTORY)
                    .exchange()
                    .expectStatus().isOk();
            client.get().uri(ServiceURI.STATISTIC)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.playerAWins").isEqualTo(0)
                    .jsonPath("$.playerBWins").isEqualTo(0)
                    .jsonPath("$.tie").isEqualTo(0)
                    .jsonPath("$.total").isEqualTo(0);
        }
    }
}