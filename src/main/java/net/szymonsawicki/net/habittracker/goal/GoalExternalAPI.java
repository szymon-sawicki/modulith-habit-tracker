package net.szymonsawicki.net.habittracker.goal;

import net.szymonsawicki.net.habittracker.UserDTO;

public interface GoalExternalAPI {
  GoalDTO addGoal(GoalDTO goalDTO);

  UserDTO findUserWithGoals(Long userId);
}
