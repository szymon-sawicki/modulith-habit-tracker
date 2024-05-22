package net.szymonsawicki.net.habittracker.habit;

import net.szymonsawicki.net.habittracker.habit.type.HabitPriority;

public record HabitDTO(Long id, Long goalId, Long userId, String name, String description, HabitPriority priority) {
}
