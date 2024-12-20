package net.szymonsawicki.net.habittracker.goalmanagement;

public record HabitDTO(
    Long id, Long goalId, Long userId, String name, String description, HabitPriority priority) {

  public HabitDTO withGoalId(Long newGoalId) {
    return new HabitDTO(id, newGoalId, userId, name, description, priority);
  }
}
