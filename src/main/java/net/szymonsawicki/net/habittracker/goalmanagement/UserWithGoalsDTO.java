package net.szymonsawicki.net.habittracker.goalmanagement;

import java.util.List;

public record UserWithGoalsDTO(Long id, String username, List<GoalDTO> userGoals) {

  public UserWithGoalsDTO withGoals(List<GoalDTO> newGoals) {
    return new UserWithGoalsDTO(this.id, this.username, newGoals);
  }
}
