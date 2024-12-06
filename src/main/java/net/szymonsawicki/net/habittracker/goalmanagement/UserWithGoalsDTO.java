package net.szymonsawicki.net.habittracker.goalmanagement;

import java.util.List;

public record UserWithGoalsDTO(Long id, String username, List<GoalDTO> userGoals) {}
