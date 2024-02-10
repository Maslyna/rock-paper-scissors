package net.roshambo.repository;

import net.roshambo.model.Status;
import net.roshambo.model.entity.Round;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RoundRepository extends ReactiveCrudRepository<Round, UUID> {
    Mono<Round> findByStatus(Status status);
}
