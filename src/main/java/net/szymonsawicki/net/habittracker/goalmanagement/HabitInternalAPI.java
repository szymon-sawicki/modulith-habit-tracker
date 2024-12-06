package net.szymonsawicki.net.habittracker.goalmanagement;

import java.util.List;

public interface HabitInternalAPI {
  HabitDTO findById(long habitId);

  List<HabitDTO> findAllHabitsForUser(long userId);

  List<HabitDTO> findAllHabitsForGoal(long goalId);

  List<HabitDTO> saveHabits(List<HabitDTO> habits);

  boolean existsById(long habitId);
}
