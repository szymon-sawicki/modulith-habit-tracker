package net.szymonsawicki.net.habittracker.habit;

import java.util.List;

public interface HabitInternalAPI {
  HabitDTO findById(long habitId);

  void deleteHabitsForUser(long userId);

  List<HabitDTO> findAllHabitsForUser(long userId);

  List<HabitDTO> findAllHabitsForGoal(long goalId);
}
