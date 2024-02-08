package net.roshambo.model.entity;

import lombok.*;
import net.roshambo.model.Move;
import net.roshambo.model.Result;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString

@Table("t_rounds")
public class Round {
    @Id
    @Column("round_id")
    private UUID roundId;

    @Column("move_a")
    private Move moveA;

    @Column("move_b")
    private Move moveB;

    @Column("result")
    private Result result;

    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}
