package net.roshambo.repository;

import net.roshambo.model.Status;
import net.roshambo.model.entity.Round;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface R2dbcTemplateRepository {
    Mono<Long> countAllWhereStatusIs(Status status);

    Flux<Round> selectAllWhereStatusNot(Status status);

    Mono<Long> countAllWhereStatusNot(Status status);

    TransactionalOperator getTransaction();
}
