package net.roshambo.repository;

import net.roshambo.model.Result;
import net.roshambo.model.entity.Round;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RoundRepository extends ReactiveCrudRepository<Round, UUID> {
    Mono<Round> findByResult(Result result);
}
