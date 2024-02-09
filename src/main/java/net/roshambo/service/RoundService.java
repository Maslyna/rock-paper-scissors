package net.roshambo.service;

import lombok.RequiredArgsConstructor;
import net.roshambo.exception.GlobalServiceException;
import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.model.Result;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.RoundRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final RoundRepository repository;

    public Mono<Round> makeMove(final Player player, final Move move) {
        return repository.findByResult(Result.ACTIVE)
                .flatMap(existingRound -> {
                    if (hasPlayerMoved(existingRound, player)) {
                        return Mono.error(new GlobalServiceException(HttpStatus.BAD_REQUEST, "Move already made"));
                    }
                    Round updatedRound = checkWinner(updateRoundWithMove(existingRound, player, move));
                    return repository.save(updatedRound);
                })
                .switchIfEmpty(repository.save(
                        updateRoundWithMove(new Round(), player, move)
                ));
    }

    public Flux<Round> getHistory() {
        return repository.findAll();
    }

    private boolean hasPlayerMoved(final Round round, final Player player) {
        return switch (player) {
            case A -> round.getMoveA() != Move.NONE;
            case B -> round.getMoveB() != Move.NONE;
        };
    }

    private Round updateRoundWithMove(final Round round, final Player player, final Move move) {
        switch (player) {
            case A -> round.setMoveA(move);
            case B -> round.setMoveB(move);
        }
        return round;
    }

    private Round checkWinner(final Round round) {
        if (round.getMoveA() != Move.NONE && round.getMoveB() != Move.NONE) {
            round.setResult(determineWinner(round.getMoveA(), round.getMoveB()));
        }
        return round;
    }

    private Result determineWinner(Move moveA, Move moveB) {
        if (moveA == moveB) {
            return Result.TIE;
        } else if (
                (moveA == Move.ROCK && moveB == Move.SCISSORS)
                        || (moveA == Move.PAPER && moveB == Move.ROCK)
                        || (moveA == Move.SCISSORS && moveB == Move.PAPER)
        ) {
            return Result.WIN_A;
        } else {
            return Result.WIN_B;
        }
    }
}

