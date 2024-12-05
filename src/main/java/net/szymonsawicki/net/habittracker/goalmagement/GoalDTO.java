package net.szymonsawicki.net.habittracker.goalmagement;

import java.util.ArrayList;
import java.util.List;

public record GoalDTO(
    Long id, Long userId, String name, String description, List<HabitDTO> habits) {

  public GoalDTO(Long id, Long userId, String name, String description) {
    this(id, userId, name, description, new ArrayList<>());
  }

  public GoalDTO withHabits(List<HabitDTO> newHabits) {
    return new GoalDTO(this.id, this.userId, this.name, this.description, newHabits);
  }

  public GoalDTO(Long id, Long userId, String name, String description, List<HabitDTO> habits) {
    this.id = id;
    this.userId = userId;
    this.name = name;
    this.description = description;
    this.habits = habits == null ? new ArrayList<>() : habits;
  }
}
