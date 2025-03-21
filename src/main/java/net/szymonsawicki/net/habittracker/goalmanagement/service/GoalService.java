package net.szymonsawicki.net.habittracker.goalmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.events.UserCreatedEvent;
import net.szymonsawicki.net.habittracker.events.UserDeletedEvent;
import net.szymonsawicki.net.habittracker.goalmanagement.*;
import net.szymonsawicki.net.habittracker.goalmanagement.GoalDTO;
import net.szymonsawicki.net.habittracker.goalmanagement.UserWithGoalsDTO;
import net.szymonsawicki.net.habittracker.goalmanagement.mapper.GoalMapper;
import net.szymonsawicki.net.habittracker.goalmanagement.model.GoalEntity;
import net.szymonsawicki.net.habittracker.goalmanagement.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.usermanagement.UserInternalAPI;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoalService implements GoalInternalAPI, GoalExternalAPI {
  private final ApplicationEventPublisher eventPublisher;
  private final GoalMapper goalMapper;
  private final GoalRepository goalRepository;
  private final HabitInternalAPI habitInternalAPI;
  private final UserInternalAPI userInternalAPI;

  @Override
  public boolean existsByGoalId(long goalId) {
    if (!goalRepository.existsById(goalId))
      throw new EntityNotFoundException("Goal with id " + goalId + " does not exist");
    return true;
  }

  @Override
  public UserWithGoalsDTO findUserWithGoals(Long userId) {

    var user = userInternalAPI.findById(userId);

    var goalsForUser = goalRepository.findByUserId(userId);

    var userWithGoals = new UserWithGoalsDTO(user.id(), user.username(), new ArrayList<>());

    if (!goalsForUser.isEmpty()) {
      userWithGoals.userGoals().addAll(goalMapper.toDtos(goalsForUser));
    }

    var goalsWithHabits =
        userWithGoals.userGoals().stream()
            .map(goal -> goal.withHabits(habitInternalAPI.findAllHabitsForGoal(goal.id())))
            .toList();

    return userWithGoals.withGoals(goalsWithHabits);
  }

  @ApplicationModuleListener
  public void onUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
    var userGoalsToCreate =
        userCreatedEvent.getGoalNames().stream()
            .map(goalName -> createGoalEntityForUser(userCreatedEvent.getId(), goalName))
            .toList();
    goalRepository.saveAll(userGoalsToCreate);
    log.info(
        "Created goals for user with id {}, Goal names: {}",
        userCreatedEvent.getId(),
        userCreatedEvent.getGoalNames());
  }

  private GoalEntity createGoalEntityForUser(Long userId, String goalName) {
    var goalEntity = new GoalEntity();
    goalEntity.setUserId(userId);
    goalEntity.setName(goalName);
    return goalEntity;
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
      goalDto = goalDto.withHabits(habitsForGoal);
    }

    return goalDto;
  }

  @Override
  public List<GoalDTO> findGoalsForUser(long userId) {

    if (!userInternalAPI.existsById(userId)) throw new EntityNotFoundException("User not exists");

    var goalsForUser = goalMapper.toDtos(goalRepository.findByUserId(userId));

    return goalsForUser.stream()
        .map(
            goal ->
                goal.withHabits(new ArrayList<>(habitInternalAPI.findAllHabitsForGoal(goal.id()))))
        .collect(Collectors.toList());
  }

  @Override
  public GoalDTO addGoal(GoalDTO goalDTO) {

    if (!userInternalAPI.existsById(goalDTO.userId()))
      throw new EntityNotFoundException("User not exists");

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
  public void onUserDeleteEvent(UserDeletedEvent event) {
    log.info("OnUserDeleteEvent. User id: {}", event.getId());
    goalRepository.deleteByUserId(event.getId());
  }
}
