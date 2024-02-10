package net.roshambo.model.dto;

import lombok.Builder;
import net.roshambo.model.Move;
import net.roshambo.model.Status;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record RoundDTO(
        UUID roundId,
        Move moveA,
        Move moveB,
        Status status,
        LocalDateTime date
) {
}
