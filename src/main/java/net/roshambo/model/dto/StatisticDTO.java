package net.roshambo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record StatisticDTO (
        @JsonProperty("playerAWins")
        Long aWins,
        @JsonProperty("playerBWins")
        Long bWins,
        Long tie,
        Long total
) {
}
