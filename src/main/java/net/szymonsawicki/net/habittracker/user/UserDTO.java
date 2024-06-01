package net.szymonsawicki.net.habittracker.user;

import java.util.ArrayList;
import java.util.List;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.user.type.UserType;

public record UserDTO(
    Long id, String username, String password, UserType userType, List<GoalDTO> goals) {
  public UserDTO(
      Long id, String username, String password, UserType userType, List<GoalDTO> goals) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.userType = userType;
    this.goals = goals == null ? new ArrayList<GoalDTO>() : goals;
  }
}
