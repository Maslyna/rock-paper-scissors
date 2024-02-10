package net.roshambo.repository.impl;

import lombok.RequiredArgsConstructor;
import net.roshambo.model.Status;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.R2dbcTemplateRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

@Repository
@RequiredArgsConstructor
public class R2dbcTemplateRepositoryImpl implements R2dbcTemplateRepository {
    private final R2dbcEntityTemplate template;
    private final ReactiveTransactionManager transactionManager;

    @Override public Mono<Long> countAllWhereStatusIs(Status status) {
        return template.count(query(where("status").is(status.value)), Round.class);
    }

    @Override public Flux<Round> selectAllWhereStatusNot(Status status) {
        return template.select(query(where("status").not(status.value)), Round.class);
    }

    @Override public Mono<Long> countAllWhereStatusNot(Status status) {
        return template.count(query(where("status").not(status.value)), Round.class);
    }

    @Override public TransactionalOperator getTransaction() {
        return TransactionalOperator.create(transactionManager);
    }
}
