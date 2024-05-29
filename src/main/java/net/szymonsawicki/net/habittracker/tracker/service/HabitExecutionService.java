package net.szymonsawicki.net.habittracker.tracker.service;

import java.util.List;
import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerExternalApi;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerInternalApi;
import net.szymonsawicki.net.habittracker.tracker.repository.HabitExecutionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabitExecutionService implements HabitTrackerExternalApi, HabitTrackerInternalApi {

  private HabitExecutionRepository habitExecutionRepository;

  public HabitExecutionService(HabitExecutionRepository habitExecutionRepository) {
    this.habitExecutionRepository = habitExecutionRepository;
  }

  @Override
  @Transactional
  public void deleteTrackingsForUser(long userId) {
    habitExecutionRepository.deleteAllByUserId(userId);
  }

  // TODO implement

  @Override
  public List<HabitExecutionDTO> findAllExecutionsByHabitId(long habitId) {
    return List.of();
  }

  @Override
  public List<HabitExecutionDTO> findAllExecutionsByUserId(long userId) {
    return List.of();
  }

  @Override
  public HabitExecutionDTO addHabitExecution(HabitExecutionDTO habitExecution) {
    return null;
  }
}
