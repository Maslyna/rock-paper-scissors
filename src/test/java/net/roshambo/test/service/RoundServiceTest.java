package net.roshambo.test.service;

import net.roshambo.model.Move;
import net.roshambo.model.Player;
import net.roshambo.model.Status;
import net.roshambo.model.entity.Round;
import net.roshambo.repository.RoundRepository;
import net.roshambo.service.impl.RoundServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoundServiceTest {

    @Mock
    private RoundRepository roundRepository;

    @InjectMocks
    private RoundServiceImpl roundService;

    @Test
    @DisplayName("should make a valid move")
    void testMakeMove() {
        Round existingRound = Round.builder()
                .moveB(Move.SCISSORS)
                .createdAt(LocalDateTime.now())
                .build();
        when(roundRepository.findByStatus(Status.ACTIVE)).thenReturn(Mono.just(existingRound));
        when(roundRepository.save(any())).thenReturn(Mono.just(existingRound));

        Round result = roundService.makeMove(Player.A, Move.ROCK).block();

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isNotNull();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getMoveA()).isNotNull();
        assertThat(result.getMoveB()).isNotNull();
    }
}