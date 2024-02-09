package net.roshambo.mapper;

import net.roshambo.model.dto.RoundDTO;
import net.roshambo.model.entity.Round;
import org.springframework.stereotype.Component;

@Component
public class RoundMapper {

    public RoundDTO roundToRoundDTO(Round round) {
        if (round == null) {
            return null;
        }
        return RoundDTO.builder()
                .roundId(round.getRoundId())
                .result(round.getResult())
                .moveA(round.getMoveA())
                .moveB(round.getMoveB())
                .date(round.getCreatedAt())
                .build();
    }
}
