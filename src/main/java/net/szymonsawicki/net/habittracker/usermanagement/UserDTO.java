package net.szymonsawicki.net.habittracker.usermanagement;

public record UserDTO(
    Long id,
    String username,
    String password,
    UserType userType,
    java.util.List<String> userGoals) {}
