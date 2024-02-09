package net.roshambo.repository;

import lombok.RequiredArgsConstructor;
import net.roshambo.model.Result;
import net.roshambo.model.entity.Round;
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
public class R2dbcTemplateRepository {
    private final R2dbcEntityTemplate template;
    private final ReactiveTransactionManager transactionManager;

    public Mono<Long> countAllWhereResultIs(Result result) {
        return template.count(query(where("result").is(result.value)), Round.class);
    }

    public Flux<Round> selectAllWhereResultNot(Result result) {
        return template.select(query(where("result").not(result.value)), Round.class);
    }

    public Mono<Long> countAllWhereResultNot(Result result) {
        return template.count(query(where("result").not(result.value)), Round.class);
    }

    public TransactionalOperator getTransaction() {
        return TransactionalOperator.create(transactionManager);
    }
}
