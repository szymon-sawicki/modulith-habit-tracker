package net.szymonsawicki.net.habittracker.tracker;

import java.util.List;

public interface HabitTrackerExternalApi {

  List<HabitExecutionDTO> findAllExecutionsByHabitId(long habitId);

  List<HabitExecutionDTO> findAllExecutionsByUserId(long userId);

  HabitExecutionDTO addHabitExecution(HabitExecutionDTO habitExecution);
}
