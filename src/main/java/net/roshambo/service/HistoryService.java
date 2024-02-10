package net.roshambo.service;

import net.roshambo.model.dto.StatisticDTO;
import net.roshambo.model.entity.Round;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Mono;

public interface HistoryService {
    Mono<Page<Round>> getHistory(PageRequest pageable);

    Mono<StatisticDTO> getStatistic();

    Mono<Void> deleteHistory();
}
