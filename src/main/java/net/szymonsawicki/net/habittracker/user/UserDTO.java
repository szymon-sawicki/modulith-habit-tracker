package net.szymonsawicki.net.habittracker.user;

import java.util.ArrayList;
import java.util.List;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;

public record UserDTO(
    Long id, String username, String password, UserType userType, List<GoalDTO> goals) {
  public UserDTO withGoals(List<GoalDTO> newGoals) {
    List<GoalDTO> combinedGoals = new ArrayList<>(goals);
    combinedGoals.addAll(newGoals);
    return new UserDTO(id, username, password, userType, combinedGoals);
  }
}
