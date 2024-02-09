package net.roshambo.service;

import lombok.RequiredArgsConstructor;
import net.roshambo.exception.handler.PlayerHasMovedException;
import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.model.Result;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.RoundRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RoundService {
    private final RoundRepository repository;

    @Transactional
    public Mono<Round> makeMove(final Player player, final Move move) {
        return repository.findByResult(Result.ACTIVE)
                .flatMap(existingRound -> {
                    if (hasPlayerMoved(existingRound, player)) {
                        return Mono.error(new PlayerHasMovedException(HttpStatus.BAD_REQUEST, "Move already made"));
                    }
                    final Round updatedRound = checkWinner(updateRoundWithMove(existingRound, player, move));
                    return repository.save(updatedRound);
                })
                .switchIfEmpty(repository.save(
                        updateRoundWithMove(new Round(), player, move)
                ));
    }

    public Mono<Page<Round>> getHistory(final PageRequest pageable) {
        return repository.findAllNotActiveRounds(pageable)
                .collectList()
                .zipWith(repository.countAllNotActiveRounds())
                .map(tuple -> new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()));
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

    private Result determineWinner(final Move moveA, final Move moveB) {
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

