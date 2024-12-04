package net.szymonsawicki.net.habittracker.goalmagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "habits")
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
