package net.szymonsawicki.net.habittracker.habit;

import java.util.List;

public interface HabitInternalAPI {
  List<HabitDTO> findAllHabitsForGoal(long goalId);

  void deleteHabitsForUser(long userId);

  List<HabitDTO> findAllHabitsForUser(long userId);
}
