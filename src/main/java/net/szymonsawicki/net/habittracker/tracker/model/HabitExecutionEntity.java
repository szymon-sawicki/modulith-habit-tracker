package net.szymonsawicki.net.habittracker.tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "habit_executions", schema = "tracker")
@Getter
@Setter
@ToString
public class HabitExecutionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long habitId;
  private Long userId;
  private Integer durationMins;
  private String comment;

  @Temporal(TemporalType.DATE)
  private LocalDate executionDate;

  @Temporal(TemporalType.TIME)
  private LocalTime executionTime;

  public LocalTime getEndTime() {
    return executionTime.plusMinutes(durationMins);
  }
}
