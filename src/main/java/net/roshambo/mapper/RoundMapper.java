package net.roshambo.mapper;

import net.roshambo.model.dto.RoundDTO;
import net.roshambo.model.entity.Round;

public interface RoundMapper {
    RoundDTO roundToRoundDTO(Round round);
}
