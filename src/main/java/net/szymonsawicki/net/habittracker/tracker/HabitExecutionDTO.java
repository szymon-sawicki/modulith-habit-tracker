package net.szymonsawicki.net.habittracker.tracker;

import java.time.LocalDate;
import java.time.LocalTime;

public record HabitExecutionDTO(
    Long id,
    Long habitId,
    Long userId,
    Integer durationMins,
    String comment,
    LocalDate executionDate,
    LocalTime executionTime) {}
