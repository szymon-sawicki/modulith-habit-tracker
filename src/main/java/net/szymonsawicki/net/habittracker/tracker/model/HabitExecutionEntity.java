package net.szymonsawicki.net.habittracker.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "habit_executions")
@Getter
@Setter
public class HabitExecutionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long habitId;
  private Long userId;
  private Integer durationMins;
  private String comment;
  private LocalDateTime executionTimestamp;
}
