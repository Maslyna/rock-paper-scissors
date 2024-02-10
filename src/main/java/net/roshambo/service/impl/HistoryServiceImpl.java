package net.roshambo.service.impl;

import lombok.RequiredArgsConstructor;
import net.roshambo.model.Status;
import net.roshambo.model.dto.StatisticDTO;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.R2dbcTemplateRepository;
import net.roshambo.repository.RoundRepository;
import net.roshambo.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final RoundRepository repository;
    private final R2dbcTemplateRepository templateRepository;

    @Override public Mono<Page<Round>> getHistory(final PageRequest pageable) {
        return templateRepository.selectAllWhereStatusNot(Status.ACTIVE)
                .collectList()
                .zipWith(templateRepository.countAllWhereStatusNot(Status.ACTIVE))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }


    @Override public Mono<StatisticDTO> getStatistic() {
        final TransactionalOperator rxtx = templateRepository.getTransaction();
        final Mono<Long> aWins = templateRepository.countAllWhereStatusIs(Status.WIN_A);
        final Mono<Long> bWins = templateRepository.countAllWhereStatusIs(Status.WIN_B);
        final Mono<Long> tie = templateRepository.countAllWhereStatusIs(Status.TIE);
        final Mono<Long> total = templateRepository.countAllWhereStatusNot(Status.ACTIVE);

        return Mono.zip(aWins, bWins, tie, total)
                .map(tuple -> StatisticDTO.builder()
                        .aWins(tuple.getT1())
                        .bWins(tuple.getT2())
                        .tie(tuple.getT3())
                        .total(tuple.getT4())
                        .build())
                .as(rxtx::transactional);
    }

    @Override public Mono<Void> deleteHistory() {
        return repository.deleteAll();
    }
}
