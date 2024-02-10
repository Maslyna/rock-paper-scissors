package net.roshambo.service;

import lombok.RequiredArgsConstructor;
import net.roshambo.exception.handler.PlayerHasMovedException;
import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.model.Status;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.RoundRepository;
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
        return repository.findByStatus(Status.ACTIVE)
                .flatMap(existingRound -> {
                    if (hasPlayerMoved(existingRound, player)) {
                        return Mono.error(new PlayerHasMovedException(HttpStatus.BAD_REQUEST, "Move already made"));
                    }
                    final Round updatedRound = updateRoundWithMove(existingRound, player, move);
                    return repository.save(updatedRound);
                })
                .switchIfEmpty(repository.save(
                        updateRoundWithMove(new Round(), player, move)
                ));
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
}

