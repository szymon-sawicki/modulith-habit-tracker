package net.szymonsawicki.net.habittracker.goalmagement;

import java.util.List;
import net.szymonsawicki.net.habittracker.usermanagement.UserDTO;
import net.szymonsawicki.net.habittracker.usermanagement.UserType;

public record UserWithGoalsDTO(
    Long id, String username, String password, UserType userType, List<UserDTO> userGoals) {}
