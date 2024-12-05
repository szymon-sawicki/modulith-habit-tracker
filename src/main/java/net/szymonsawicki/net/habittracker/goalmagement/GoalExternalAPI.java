package net.szymonsawicki.net.habittracker.goalmagement;

public interface GoalExternalAPI {
  GoalDTO addGoal(GoalDTO goalDTO);

  UserWithGoalsDTO findUserWithGoals(Long userId);
}
