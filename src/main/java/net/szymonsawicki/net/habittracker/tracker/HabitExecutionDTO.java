package net.szymonsawicki.net.habittracker.tracker;

import java.time.LocalDateTime;

public record HabitExecutionDTO(Long id, Long habitId, Long userId, Integer durationMin, String comment, LocalDateTime executionTimestamp) {
}
