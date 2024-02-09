package net.roshambo.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.roshambo.mapper.RoundMapper;
import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.model.dto.RoundDTO;
import net.roshambo.service.RoundService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
@Validated
public class GameController {
    private final RoundService service;
    private final RoundMapper mapper;

    @PostMapping()
    public Mono<Void> makeMove(@RequestParam("player") Player player, @RequestParam("move") Move move) {
        return service.makeMove(player, move)
                .then();
    }

    @GetMapping()
    public Mono<Page<RoundDTO>> history(
            @RequestParam(value = "size", defaultValue = "10") @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(value = "page", defaultValue = "0") @PositiveOrZero Integer pageNum,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "order can be only ASC or DESC"
            )
            String order,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String... sortBy
    ) {
        return service.getHistory(
                PageRequest.of(pageNum, pageSize, Sort.Direction.valueOf(order), sortBy)
        ).map(page -> page.map(mapper::roundToRoundDTO));
    }
}
