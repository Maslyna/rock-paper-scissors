package net.roshambo.test.service;

import net.roshambo.model.Move;
import net.roshambo.model.Status;
import net.roshambo.model.dto.StatisticDTO;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.R2dbcTemplateRepository;
import net.roshambo.repository.RoundRepository;
import net.roshambo.service.HistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTest {

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private R2dbcTemplateRepository templateRepository;
    @Mock
    private ReactiveTransactionManager transactionManager;

    @InjectMocks
    private HistoryService historyService;

    @Test
    @DisplayName("should get history with specified parameters")
    void testGetHistory() {
        final Round round = Round.builder()
                .roundId(UUID.randomUUID())
                .moveA(Move.PAPER)
                .moveB(Move.SCISSORS)
                .status(Status.WIN_B)
                .build();
        final List<Round> mockRounds = List.of(round);
        final PageRequest pageable = PageRequest.of(0, 10);
        final Page<Round> expectedResult = new PageImpl<>(mockRounds, pageable, mockRounds.size());

        when(templateRepository.selectAllWhereStatusNot(Status.ACTIVE)).thenReturn(Flux.fromIterable(mockRounds));
        when(templateRepository.countAllWhereStatusNot(Status.ACTIVE)).thenReturn(Mono.just((long) mockRounds.size()));


        Page<Round> result = historyService.getHistory(pageable).block();

        assertThat(result).isEqualTo(expectedResult);
    }

//    @Test
//    @DisplayName("should return statistic")
//    void testGetStatistic() {
//        final long winACount = 10L;
//        final long winBCount = 5L;
//        final long tieCount = 2L;
//        final long total = winACount + winBCount + tieCount;
//
//        when(templateRepository.countAllWhereStatusIs(Status.WIN_A)).thenReturn(Mono.just(winACount));
//        when(templateRepository.countAllWhereStatusIs(Status.WIN_B)).thenReturn(Mono.just(winBCount));
//        when(templateRepository.countAllWhereStatusIs(Status.TIE)).thenReturn(Mono.just(tieCount));
//        when(templateRepository.countAllWhereStatusNot(Status.ACTIVE)).thenReturn(Mono.just(total));
//        when(templateRepository.getTransaction()).thenReturn(new TransactionalOperator() {
//            @Override
//            public <T> Flux<T> execute(TransactionCallback<T> action) throws TransactionException {
//                return Flux.just();
//            }
//        });
//
//        StatisticDTO result = historyService.getStatistic().block(Duration.of(5, ChronoUnit.SECONDS));
////
////        assertThat(result.tie()).isNotNull().isEqualTo(tieCount);
////        assertThat(result.aWins()).isNotNull().isEqualTo(winACount);
////        assertThat(result.bWins()).isNotNull().isEqualTo(winBCount);
////        assertThat(result.total()).isNotNull().isEqualTo(total);
//    }


    @Test
    @DisplayName("should delete history")
    void testDeleteHistory() {
        final Mono<Void> expectedResult = Mono.empty();
        when(roundRepository.deleteAll()).thenReturn(expectedResult);

        Mono<Void> result = historyService.deleteHistory();

        assertThat(result).isEqualTo(expectedResult);
    }
}