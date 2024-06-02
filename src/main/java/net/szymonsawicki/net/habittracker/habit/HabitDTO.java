package net.szymonsawicki.net.habittracker.habit;

public record HabitDTO(
    Long id, Long goalId, Long userId, String name, String description, HabitPriority priority) {}
