package net.szymonsawicki.net.habittracker.tracker.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerExternalApi;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerInternalApi;
import net.szymonsawicki.net.habittracker.tracker.mapper.HabitExecutionMapper;
import net.szymonsawicki.net.habittracker.tracker.model.HabitExecutionEntity;
import net.szymonsawicki.net.habittracker.tracker.repository.HabitExecutionRepository;
import net.szymonsawicki.net.habittracker.tracker.service.exception.HabitExecutionException;
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

  public HabitExecutionService(
      UserInternalAPI userInternalAPI,
      HabitInternalAPI habitInternalAPI,
      HabitExecutionRepository habitExecutionRepository,
      HabitExecutionMapper habitExecutionMapper) {
    this.userInternalAPI = userInternalAPI;
    this.habitInternalAPI = habitInternalAPI;
    this.habitExecutionRepository = habitExecutionRepository;
    this.habitExecutionMapper = habitExecutionMapper;
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

    userInternalAPI.existsById(habitExecution.userId());
    habitInternalAPI.existsById(habitExecution.habitId());

    var entityToInsert = habitExecutionMapper.toEntity(habitExecution);

    if (isAnyExecutionOverlapping(entityToInsert)) {
      throw new HabitExecutionException("Habit execution is overlapping with exisitng executions");
    }

    var addedExecution = habitExecutionRepository.save(entityToInsert);

    log.info(String.format("Added habit execution: %s", addedExecution));

    return habitExecutionMapper.toDto(addedExecution);
  }

  private boolean isAnyExecutionOverlapping(HabitExecutionEntity executionEntity) {
    return habitExecutionRepository
        .findAllByUserIdAndExecutionDate(
            executionEntity.getUserId(), executionEntity.getExecutionDate())
        .stream()
        .anyMatch(
            e -> isOverlapping(executionEntity, e) || areTimesWithinRange(e, executionEntity));
  }

  private static boolean isOverlapping(
      HabitExecutionEntity newExecution, HabitExecutionEntity existingExecution) {
    return newExecution.getExecutionTime().equals(existingExecution.getExecutionTime())
        || (newExecution.getExecutionTime().isBefore(existingExecution.getExecutionTime())
            && newExecution.getEndTime().isAfter(existingExecution.getExecutionTime()))
        || (newExecution.getExecutionTime().isBefore(existingExecution.getEndTime())
            && newExecution.getEndTime().isAfter(existingExecution.getEndTime()));
  }

  private boolean areTimesWithinRange(
      HabitExecutionEntity existingEntity, HabitExecutionEntity entityToCheck) {
    return (entityToCheck.getExecutionTime().isAfter(existingEntity.getExecutionTime())
            && entityToCheck.getExecutionTime().isBefore(existingEntity.getEndTime())
        || (entityToCheck.getEndTime().isAfter(existingEntity.getExecutionTime())
            && entityToCheck.getEndTime().isBefore(existingEntity.getEndTime())));
  }
}
