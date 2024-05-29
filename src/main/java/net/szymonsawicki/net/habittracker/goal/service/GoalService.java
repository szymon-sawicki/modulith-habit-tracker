package net.szymonsawicki.net.habittracker.goal.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.GoalExternalAPI;
import net.szymonsawicki.net.habittracker.goal.GoalInternalAPI;
import net.szymonsawicki.net.habittracker.goal.mapper.GoalMapper;
import net.szymonsawicki.net.habittracker.goal.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class GoalService implements GoalInternalAPI, GoalExternalAPI {
  private GoalMapper goalMapper;
  private GoalRepository goalRepository;
  private HabitInternalAPI habitInternalAPI;

  public GoalService(
      GoalMapper goalMapper, GoalRepository goalRepository, HabitInternalAPI habitInternalAPI) {
    this.goalMapper = goalMapper;
    this.goalRepository = goalRepository;
    this.habitInternalAPI = habitInternalAPI;
  }

  @Override
  public GoalDTO findGoalWithHabits(long goalId) {
    var goal =
        goalRepository
            .findById(goalId)
            .orElseThrow(() -> new EntityNotFoundException("Goal can't be found"));

    var goalDto = goalMapper.toDto(goal);

    goalDto.habits().addAll(habitInternalAPI.findAllHabitsForGoal(goalId));

    return goalDto;
  }

  @Override
  public List<GoalDTO> findGoalsForUser(long userId) {

    var goalsForUser = goalMapper.toDtos(goalRepository.findByUserId(userId));

    goalsForUser.forEach(
        goal -> goal.habits().addAll(habitInternalAPI.findAllHabitsForGoal(goal.id())));

    return goalsForUser;
  }

  @Override
  public GoalDTO addGoal(GoalDTO goalDTO) {
    var addedGoal = goalRepository.save(goalMapper.toEntity(goalDTO));
    log.info(String.format("Added goal: %s", addedGoal));
    return goalMapper.toDto(addedGoal);
  }

  @Override
  @Transactional
  public void deleteGoalsForUser(long userId) {
    goalRepository.deleteByUserId(userId);
  }
}
