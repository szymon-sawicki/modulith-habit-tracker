package net.szymonsawicki.net.habittracker.habit;

import java.util.List;

public interface HabitInternalAPI {
  List<HabitDTO> findAllHabitsForGoal(long goalId);

  List<HabitDTO> findAllHabitsForUser(long userId);
}
