package net.szymonsawicki.net.habittracker.goal;

import java.util.ArrayList;
import java.util.List;
import net.szymonsawicki.net.habittracker.habit.HabitDTO;

public record GoalDTO(
    Long id, Long userId, String name, String description, List<HabitDTO> habits) {
  public GoalDTO(Long id, Long userId, String name, String description) {
    this(id, userId, name, description, new ArrayList<>());
  }
}
