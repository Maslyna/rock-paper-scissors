package net.roshambo.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import net.roshambo.mapper.RoundMapper;
import net.roshambo.model.dto.RoundDTO;
import net.roshambo.model.dto.StatisticDTO;
import net.roshambo.service.HistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/games/history")
@RequiredArgsConstructor
@Validated
public class HistoryController {
    private final HistoryService historyService;
    private final RoundMapper mapper;

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
        return historyService.getHistory(
                PageRequest.of(pageNum, pageSize, Sort.Direction.valueOf(order.toUpperCase()), sortBy)
        ).map(page -> page.map(mapper::roundToRoundDTO));
    }

    @GetMapping("/statistic")
    public Mono<StatisticDTO> statistic() {
        return historyService.getStatistic();
    }

    @DeleteMapping()
    public Mono<Void> clearHistory() {
        return historyService.deleteHistory();
    }
}
