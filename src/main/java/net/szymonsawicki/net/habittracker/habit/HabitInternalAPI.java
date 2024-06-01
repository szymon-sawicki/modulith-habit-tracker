package net.szymonsawicki.net.habittracker.habit;

import java.util.List;

public interface HabitInternalAPI {
  HabitDTO findById(long habitId);

  void deleteHabitsForUser(long userId);

  List<HabitDTO> findAllHabitsForUser(long userId);

  List<HabitDTO> findAllHabitsForGoal(long goalId);

  List<HabitDTO> saveHabits(List<HabitDTO> habits);

  boolean existsById(long habitId);
}
