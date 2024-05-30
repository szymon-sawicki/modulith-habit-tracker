package net.szymonsawicki.net.habittracker.tracker;

public interface HabitTrackerInternalApi {
  HabitExecutionDTO addHabitExecution(HabitExecutionDTO habitExecution);

  void deleteTrackingsForUser(long userId);
}
