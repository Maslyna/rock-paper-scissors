package net.roshambo.controller;

import lombok.RequiredArgsConstructor;
import net.roshambo.exception.GlobalServiceException;
import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.model.entity.Round;
import net.roshambo.service.RoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {
    private final RoundService service;

    @PostMapping()
    public Mono<Object> makeMove(@RequestParam("player") Player player, @RequestParam("move") Move move) {
        return service.makeMove(player, move)
                .cast(Object.class)
                .onErrorResume(GlobalServiceException.class, this::createResponse);
    }

    @GetMapping()
    public Flux<Round> history() {
        return service.getHistory();
    }

    private Mono<Object> createResponse(GlobalServiceException err) {
        return Mono.just(ResponseEntity.status(err.getStatusCode()).body(err.getBody()));
    }
}
