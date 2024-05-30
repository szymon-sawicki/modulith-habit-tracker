package net.szymonsawicki.net.habittracker.tracker.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerExternalApi;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerInternalApi;
import net.szymonsawicki.net.habittracker.tracker.mapper.HabitExecutionMapper;
import net.szymonsawicki.net.habittracker.tracker.repository.HabitExecutionRepository;
import net.szymonsawicki.net.habittracker.user.UserInternalAPI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class HabitExecutionService implements HabitTrackerExternalApi, HabitTrackerInternalApi {

  private UserInternalAPI userInternalAPI;
  private HabitInternalAPI habitInternalAPI;
  private HabitExecutionRepository habitExecutionRepository;

  private HabitExecutionMapper habitExecutionMapper;

  public HabitExecutionService(HabitExecutionRepository habitExecutionRepository) {
    this.habitExecutionRepository = habitExecutionRepository;
  }

  @Override
  @Transactional
  public void deleteTrackingsForUser(long userId) {
    habitExecutionRepository.deleteAllByUserId(userId);
  }

  @Override
  public List<HabitExecutionDTO> findAllExecutionsByHabitId(long habitId) {
    return habitExecutionMapper.toDtos(habitExecutionRepository.findAllByHabitId(habitId));
  }

  @Override
  public List<HabitExecutionDTO> findAllExecutionsByUserId(long userId) {
    return habitExecutionMapper.toDtos(habitExecutionRepository.findAllByUserId(userId));
  }

  @Override
  public HabitExecutionDTO addHabitExecution(HabitExecutionDTO habitExecution) {

    var user = userInternalAPI.findById(habitExecution.userId());
    var habit = habitInternalAPI.findById(habitExecution.habitId());

    var addedExecution =
        habitExecutionRepository.save(habitExecutionMapper.toEntity(habitExecution));

    log.info(String.format("Added habit execution: %s", addedExecution));

    // TODO add some necessary logic (date overlapping for example)

    return habitExecutionMapper.toDto(addedExecution);
  }
}
