package net.szymonsawicki.net.habittracker.habit.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.goal.GoalInternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitDTO;
import net.szymonsawicki.net.habittracker.habit.HabitExternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.habit.mapper.HabitMapper;
import net.szymonsawicki.net.habittracker.habit.model.HabitEntity;
import net.szymonsawicki.net.habittracker.habit.repository.HabitRepository;
import net.szymonsawicki.net.habittracker.user.UserInternalAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HabitService implements HabitExternalAPI, HabitInternalAPI {
  private HabitRepository habitRepository;
  private HabitMapper habitMapper;
  private UserInternalAPI userInternalAPI;
  private GoalInternalAPI goalInternalAPI;

  @Autowired
  @Lazy
  public void setGoalInternalAPI(GoalInternalAPI goalInternalAPI) {
    this.goalInternalAPI = goalInternalAPI;
  }

  @Autowired
  @Lazy
  public void setUserInternalAPI(UserInternalAPI userInternalAPI) {
    this.userInternalAPI = userInternalAPI;
  }

  public HabitService(HabitRepository habitRepository, HabitMapper habitMapper) {
    this.habitRepository = habitRepository;
    this.habitMapper = habitMapper;
  }

  @Override
  public HabitDTO addHabit(HabitDTO habit) {
    userInternalAPI.existsById(habit.userId());
    var savedHabit = habitRepository.save(habitMapper.toEntity(habit));
    log.info(String.format("Added habit: %s", savedHabit));
    return habitMapper.toDto(savedHabit);
  }

  @Override
  public List<HabitDTO> findAllHabitsForGoal(long goalId) {
    return habitMapper.toDtos(habitRepository.findAllByGoalId(goalId));
  }

  @Override
  public void deleteHabitsForUser(long userId) {
    habitRepository.deleteAllByUserId(userId);
  }

  @Override
  public List<HabitDTO> findAllHabitsForUser(long userId) {
    return habitMapper.toDtos(habitRepository.findAllByUserId(userId));
  }

  @Override
  public HabitDTO findById(long habitId) {

    var habit =
        habitRepository
            .findById(habitId)
            .orElseThrow(() -> new EntityNotFoundException("Goal can't be found"));

    return habitMapper.toDto(habit);
  }

  @Override
  public List<HabitDTO> saveHabits(List<HabitDTO> habits) {

    var savedHabits = habitRepository.saveAll(habitMapper.toEntities(habits));

    log.info(String.format("Added habits: %s", savedHabits));

    return habitMapper.toDtos((List<HabitEntity>) savedHabits);
  }

  @Override
  public boolean existsById(long habitId) {
    if (!habitRepository.existsById(habitId)) {
      throw new EntityNotFoundException("Habit can't be found");
    }
    return true;
  }
}
