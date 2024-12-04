package net.szymonsawicki.net.habittracker.goalmagement;

import net.szymonsawicki.net.habittracker.usermanagement.UserDTO;

public interface GoalExternalAPI {
  GoalDTO addGoal(GoalDTO goalDTO);

  UserDTO findUserWithGoals(Long userId);
}
