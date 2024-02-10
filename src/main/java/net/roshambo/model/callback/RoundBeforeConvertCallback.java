package net.roshambo.model.callback;

import net.roshambo.model.Move;
import net.roshambo.model.Status;
import net.roshambo.model.entity.Round;
import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RoundBeforeConvertCallback implements BeforeConvertCallback<Round> {
    @Override
    public Publisher<Round> onBeforeConvert(Round entity, SqlIdentifier table) {
        return Mono.just(checkWinner(entity));
    }

    private Round checkWinner(final Round round) {
        if (round.getMoveA() != Move.NONE && round.getMoveB() != Move.NONE) {
            round.setStatus(determineWinner(round.getMoveA(), round.getMoveB()));
        }
        return round;
    }

    private Status determineWinner(final Move moveA, final Move moveB) {
        if (moveA == moveB) {
            return Status.TIE;
        } else if (
                (moveA == Move.ROCK && moveB == Move.SCISSORS)
                        || (moveA == Move.PAPER && moveB == Move.ROCK)
                        || (moveA == Move.SCISSORS && moveB == Move.PAPER)
        ) {
            return Status.WIN_A;
        } else {
            return Status.WIN_B;
        }
    }
}
