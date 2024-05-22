package net.szymonsawicki.net.habittracker.tracker;

import net.szymonsawicki.net.habittracker.habit.HabitDTO;

import java.util.HashMap;

public record UserTrackerDTO(Long userId, HashMap<HabitDTO,HabitExecutionDTO> habitExecutions) {
}
