package net.roshambo.service;

import lombok.RequiredArgsConstructor;
import net.roshambo.model.Result;
import net.roshambo.model.dto.StatisticDTO;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.R2dbcTemplateRepository;
import net.roshambo.repository.RoundRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final RoundRepository repository;
    private final R2dbcTemplateRepository templateRepository;

    public Mono<Page<Round>> getHistory(final PageRequest pageable) {
        return templateRepository.selectAllWhereResultNot(Result.ACTIVE)
                .collectList()
                .zipWith(templateRepository.countAllWhereResultNot(Result.ACTIVE))
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
    }


    public Mono<StatisticDTO> getStatistic() {
        final TransactionalOperator rxtx = templateRepository.getTransaction();
        final Mono<Long> aWins = templateRepository.countAllWhereResultIs(Result.WIN_A);
        final Mono<Long> bWins = templateRepository.countAllWhereResultIs(Result.WIN_B);
        final Mono<Long> tie = templateRepository.countAllWhereResultIs(Result.TIE);
        final Mono<Long> total = templateRepository.countAllWhereResultNot(Result.ACTIVE);

        return Mono.zip(aWins, bWins, tie, total)
                .map(tuple -> StatisticDTO.builder()
                        .aWins(tuple.getT1())
                        .bWins(tuple.getT2())
                        .tie(tuple.getT3())
                        .total(tuple.getT4())
                        .build())
                .as(rxtx::transactional);
    }

    public Mono<Void> deleteHistory() {
        return repository.deleteAll();
    }
}
