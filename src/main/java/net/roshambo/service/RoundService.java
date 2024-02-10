package net.roshambo.service;

import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.model.entity.Round;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface RoundService {
    @Transactional
    Mono<Round> makeMove(Player player, Move move);

    Mono<Void> generateRounds(int range);
}
