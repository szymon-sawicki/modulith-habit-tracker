package net.szymonsawicki.net.habittracker.goalmanagement;

public interface GoalExternalAPI {
  GoalDTO addGoal(GoalDTO goalDTO);

  UserWithGoalsDTO findUserWithGoals(Long userId);
}
