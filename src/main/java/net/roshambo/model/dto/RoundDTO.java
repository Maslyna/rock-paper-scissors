package net.roshambo.model.dto;

import lombok.Builder;
import net.roshambo.model.Move;
import net.roshambo.model.Result;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record RoundDTO(
        UUID roundId,
        Move moveA,
        Move moveB,
        Result result,
        LocalDateTime date
) {
}
