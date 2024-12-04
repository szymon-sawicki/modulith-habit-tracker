package net.szymonsawicki.net.habittracker.goalmagement;

import java.util.List;

public interface HabitExternalAPI {
  HabitDTO addHabit(HabitDTO habit);

  List<HabitDTO> findAllHabitsForUser(long userId);

  List<HabitDTO> findAllHabitsForGoal(long goalId);
}
