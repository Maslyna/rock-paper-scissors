package net.roshambo.test.controller;

import net.roshambo.controller.HistoryController;
import net.roshambo.mapper.RoundMapper;
import net.roshambo.model.dto.RoundDTO;
import net.roshambo.model.dto.StatisticDTO;
import net.roshambo.model.entity.Round;
import net.roshambo.service.HistoryService;
import net.roshambo.test.ServiceURI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(HistoryController.class)
public class HistoryControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private HistoryService historyService;

    @MockBean
    private RoundMapper mapper;

    @Test
    @DisplayName("should return history with specified parameters")
    void testHistoryEndpoint() {
        final int page = 0;
        final int size = 10;
        final Sort.Direction direction = Sort.Direction.DESC;
        final String sortBy = "createdAt";
        final Pageable pageable = PageRequest.of(page, size, direction, sortBy);
        final Page<Round> mockPage = new PageImpl<>(List.of(), pageable, 0);
        final Page<RoundDTO> expectedResult = new PageImpl<>(List.of(), pageable, 0);

        when(historyService.getHistory(any(PageRequest.class))).thenReturn(Mono.just(mockPage));
        when(mapper.roundToRoundDTO(any(Round.class))).thenReturn(RoundDTO.builder().build());

        client.get().uri(builder -> builder.path(ServiceURI.HISTORY)
                        .queryParam("size", size)
                        .queryParam("page", page)
                        .queryParam("orderBy", direction)
                        .queryParam("sortBy", sortBy)
                        .build()
                ).exchange()
                .expectBody()
                .equals(expectedResult);
    }

    @Test
    @DisplayName("should return statistic")
    void testStatisticEndpoint() {
        StatisticDTO mockStatistic = StatisticDTO.builder()
                .total(0L)
                .tie(0L)
                .bWins(0L)
                .aWins(0L)
                .build();
        when(historyService.getStatistic()).thenReturn(Mono.just(mockStatistic));

        client.get().uri(ServiceURI.STATISTIC)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StatisticDTO.class).isEqualTo(mockStatistic);
    }

    @Test
    @DisplayName("should return 200")
    void testClearHistory() {
        when(historyService.deleteHistory()).thenReturn(Mono.empty());

        client.delete().uri(ServiceURI.CLEAR_HISTORY)
                .exchange()
                .expectStatus()
                .isOk();
    }

}
