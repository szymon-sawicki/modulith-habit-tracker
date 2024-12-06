package net.szymonsawicki.net.habittracker.goalmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "habits", schema = "goal_habit")
@Getter
@Setter
@ToString
public class HabitEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long goalId;
  private Long userId;
  private String name;
  private String description;
}
