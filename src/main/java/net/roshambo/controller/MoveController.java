package net.roshambo.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.service.RoundService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
@Validated
public class MoveController {
    private final RoundService roundService;

    @PostMapping()
    public Mono<Void> makeMove(@RequestParam("player") Player player,
                               @RequestParam("move")
                               @Pattern(
                                       regexp = "rock|paper|scissors",
                                       flags = {Pattern.Flag.CASE_INSENSITIVE},
                                       message = "move can be only: [ROCK, PAPER, SCISSORS]"
                               )
                               String move) {
        return roundService.makeMove(player, Move.valueOf(move))
                .then();
    }
}
