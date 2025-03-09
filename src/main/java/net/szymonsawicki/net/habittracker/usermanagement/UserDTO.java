package net.szymonsawicki.net.habittracker.usermanagement;

import java.util.ArrayList;

public record UserDTO(
    Long id,
    String username,
    String password,
    UserType userType,
    java.util.List<String> userGoals) {}
