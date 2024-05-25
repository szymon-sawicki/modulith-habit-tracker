package net.szymonsawicki.net.habittracker.user;

import java.util.List;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.user.type.UserType;

public record UserDTO(
    Long id, String username, String password, UserType userType, List<GoalDTO> goals) {}
