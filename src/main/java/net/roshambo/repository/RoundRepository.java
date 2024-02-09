package net.roshambo.repository;

import net.roshambo.model.Result;
import net.roshambo.model.entity.Round;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RoundRepository extends ReactiveCrudRepository<Round, UUID> {
    Mono<Round> findByResult(Result result);
    @Query("SELECT * FROM t_rounds WHERE result != -1")
    Flux<Round> findAllNotActiveRounds(Pageable pageable);
    @Query("SELECT COUNT(*) FROM t_rounds WHERE result != -1")
    Mono<Long> countAllNotActiveRounds();

}
