package net.szymonsawicki.net.habittracker.tracker;

import java.util.List;
import java.util.Map;
import net.szymonsawicki.net.habittracker.goalmanagement.HabitDTO;

public record UserTrackerDTO(Long userId, Map<HabitDTO, List<HabitExecutionDTO>> habitExecutions) {}
