package net.szymonsawicki.net.habittracker.goal.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "goals")
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
