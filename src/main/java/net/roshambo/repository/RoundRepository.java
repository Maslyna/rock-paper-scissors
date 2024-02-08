package net.roshambo.repository;

import net.roshambo.model.entity.Round;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface RoundRepository extends ReactiveCrudRepository<Round, UUID> {
}
