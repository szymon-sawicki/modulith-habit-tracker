package net.szymonsawicki.net.habittracker.goal.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.GoalExistsEvent;
import net.szymonsawicki.net.habittracker.UserDeleteEvent;
import net.szymonsawicki.net.habittracker.UserExistsEvent;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.GoalExternalAPI;
import net.szymonsawicki.net.habittracker.goal.GoalInternalAPI;
import net.szymonsawicki.net.habittracker.goal.mapper.GoalMapper;
import net.szymonsawicki.net.habittracker.goal.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalService implements GoalInternalAPI, GoalExternalAPI {
  private final ApplicationEventPublisher eventPublisher;
  private final GoalMapper goalMapper;
  private final GoalRepository goalRepository;
  private final HabitInternalAPI habitInternalAPI;

  @Override
  public boolean existsByGoalId(long goalId) {
    if (!goalRepository.existsById(goalId))
      throw new EntityNotFoundException("Goal with id " + goalId + " does not exist");
    return true;
  }

  @Override
  public GoalDTO findGoalWithHabits(long goalId) {
    var goal =
        goalRepository
            .findById(goalId)
            .orElseThrow(() -> new EntityNotFoundException("Goal can't be found"));

    var goalDto = goalMapper.toDto(goal);

    var habitsForGoal = habitInternalAPI.findAllHabitsForGoal(goalId);

    if (!habitsForGoal.isEmpty()) {
      goalDto.habits().addAll(habitsForGoal);
    }

    return goalDto;
  }

  @Override
  public List<GoalDTO> findGoalsForUser(long userId) {

    eventPublisher.publishEvent(new UserExistsEvent(userId));

    var goalsForUser = goalMapper.toDtos(goalRepository.findByUserId(userId));

    goalsForUser.forEach(
        goal -> goal.habits().addAll(habitInternalAPI.findAllHabitsForGoal(goal.id())));

    return goalsForUser;
  }

  @Override
  public GoalDTO addGoal(GoalDTO goalDTO) {

    eventPublisher.publishEvent(new UserExistsEvent(goalDTO.userId()));

    var addedGoal = goalRepository.save(goalMapper.toEntity(goalDTO));

    if (!goalDTO.habits().isEmpty()) {
      var habitsWithGoalId =
          goalDTO.habits().stream().map(habit -> habit.withGoalId(addedGoal.getId())).toList();
      var savedHabits = habitInternalAPI.saveHabits(habitsWithGoalId);
    }

    log.info(String.format("Added goal: %s", addedGoal));

    return goalMapper.toDto(addedGoal);
  }

  @EventListener
  public void onGoalExistsEvent(GoalExistsEvent event) {
    log.info("OnGoalExistsEvent. Goal id: {}", event.getId());
    existsByGoalId(event.getId());
  }

  @EventListener
  public void onUserDeleteEvent(UserDeleteEvent event) {
    log.info("OnUserDeleteEvent. User id: {}", event.getId());
    goalRepository.deleteByUserId(event.getId());
  }
}
