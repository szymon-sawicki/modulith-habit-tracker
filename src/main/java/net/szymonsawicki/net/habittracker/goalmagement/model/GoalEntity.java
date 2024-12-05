package net.szymonsawicki.net.habittracker.goalmagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "goals", schema = "goal_habit")
@Getter
@Setter
@ToString
public class GoalEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;
  private String name;
  private String description;
}
