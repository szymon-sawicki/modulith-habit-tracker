package net.szymonsawicki.net.habittracker.tracker;

import java.util.HashMap;
import net.szymonsawicki.net.habittracker.habit.HabitDTO;

public record UserTrackerDTO(Long userId, HashMap<HabitDTO, HabitExecutionDTO> habitExecutions) {}
