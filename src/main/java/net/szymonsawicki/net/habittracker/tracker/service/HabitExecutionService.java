package net.szymonsawicki.net.habittracker.tracker.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.events.UserDeletedEvent;
import net.szymonsawicki.net.habittracker.goalmagement.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerExternalApi;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerInternalApi;
import net.szymonsawicki.net.habittracker.tracker.UserTrackerDTO;
import net.szymonsawicki.net.habittracker.tracker.mapper.HabitExecutionMapper;
import net.szymonsawicki.net.habittracker.tracker.model.HabitExecutionEntity;
import net.szymonsawicki.net.habittracker.tracker.repository.HabitExecutionRepository;
import net.szymonsawicki.net.habittracker.tracker.service.exception.HabitExecutionException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HabitExecutionService implements HabitTrackerExternalApi, HabitTrackerInternalApi {

  private final ApplicationEventPublisher eventPublisher;
  private final HabitExecutionRepository habitExecutionRepository;
  private final HabitExecutionMapper habitExecutionMapper;
  private final HabitInternalAPI habitInternalAPI;

  @ApplicationModuleListener
  public void onDeleteTrackingsForUser(UserDeletedEvent event) {
    log.info("OnUserDeleteEvent. User id: {}", event.getId());
    habitExecutionRepository.deleteAllByUserId(event.getId());
  }

  @Override
  public List<HabitExecutionDTO> findAllExecutionsByHabitId(long habitId) {

    if (habitInternalAPI.existsById(habitId)) throw new EntityNotFoundException("Habit not exists");

    return habitExecutionMapper.toDtos(habitExecutionRepository.findAllByHabitId(habitId));
  }

  @Override
  public List<HabitExecutionDTO> findAllExecutionsByUserId(long userId) {

    var executionsForUser = habitExecutionRepository.findAllByUserId(userId);

    if (executionsForUser.isEmpty()) {
      throw new HabitExecutionException("No exeution for user with id " + userId + " found");
    }
    return habitExecutionMapper.toDtos(executionsForUser);
  }

  @Override
  public HabitExecutionDTO addHabitExecution(HabitExecutionDTO habitExecution) {

    if (habitInternalAPI.existsById(habitExecution.habitId()))
      throw new EntityNotFoundException("Habit not exists");

    var entityToInsert = habitExecutionMapper.toEntity(habitExecution);

    if (isAnyExecutionOverlapping(entityToInsert)) {
      throw new HabitExecutionException("Habit execution is overlapping with exisitng executions");
    }

    var addedExecution = habitExecutionRepository.save(entityToInsert);
    log.info(String.format("Added habit execution: %s", addedExecution));

    return habitExecutionMapper.toDto(addedExecution);
  }

  @Override
  public UserTrackerDTO getUserTracker(long userId) {

    var executionsHashmap =
        habitInternalAPI.findAllHabitsForUser(userId).stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    e ->
                        habitExecutionMapper.toDtos(
                            habitExecutionRepository.findAllByHabitId(e.id()))));

    return new UserTrackerDTO(userId, executionsHashmap);
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
