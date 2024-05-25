package net.szymonsawicki.net.habittracker.goal;

import java.util.List;

public interface GoalInternalAPI {
  GoalDTO findGoalWithHabits(long goalId);

  void deleteGoalsForUser(long userId);

  List<GoalDTO> findGoalsForUser(long userId);
}
