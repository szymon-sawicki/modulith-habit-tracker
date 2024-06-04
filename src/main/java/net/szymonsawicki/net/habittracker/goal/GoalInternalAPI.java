package net.szymonsawicki.net.habittracker.goal;

import java.util.List;

public interface GoalInternalAPI {
  GoalDTO findGoalWithHabits(long goalId);

  List<GoalDTO> findGoalsForUser(long userId);

  boolean existsByGoalId(long goalId);
}
