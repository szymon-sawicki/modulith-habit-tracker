package net.szymonsawicki.net.habittracker.goalmagement;

import java.util.List;

public interface GoalInternalAPI {
  GoalDTO findGoalWithHabits(long goalId);

  List<GoalDTO> findGoalsForUser(long userId);

  boolean existsByGoalId(long goalId);
}
