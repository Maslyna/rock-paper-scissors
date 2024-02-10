package net.roshambo.test.controller;

import net.roshambo.controller.MoveController;
import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.service.RoundService;
import net.roshambo.test.ServiceURI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(
        controllers = MoveController.class
)
public class MoveControllerTests {

    @MockBean
    private RoundService service;
    @Autowired
    private WebTestClient client;


    @Test
    @DisplayName("should make a valid move")
    void testMakeMoveEndpoint() {
        Player player = Player.A;
        String validMove = "rock";
        when(service.makeMove(eq(player), eq(Move.ROCK))).thenReturn(Mono.empty());

        client.post().uri(builder -> builder.path(ServiceURI.MAKE_MOVE)
                        .queryParam("player", player.name())
                        .queryParam("move", validMove)
                        .build()
                ).exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }
}
