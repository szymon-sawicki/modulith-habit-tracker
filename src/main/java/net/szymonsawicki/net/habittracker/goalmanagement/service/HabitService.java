package net.szymonsawicki.net.habittracker.goalmanagement.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.events.UserDeletedEvent;
import net.szymonsawicki.net.habittracker.goalmanagement.HabitDTO;
import net.szymonsawicki.net.habittracker.goalmanagement.HabitExternalAPI;
import net.szymonsawicki.net.habittracker.goalmanagement.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.goalmanagement.mapper.HabitMapper;
import net.szymonsawicki.net.habittracker.goalmanagement.model.HabitEntity;
import net.szymonsawicki.net.habittracker.goalmanagement.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.goalmanagement.repository.HabitRepository;
import net.szymonsawicki.net.habittracker.usermanagement.UserInternalAPI;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HabitService implements HabitExternalAPI, HabitInternalAPI {
  private final ApplicationEventPublisher eventPublisher;
  private final HabitRepository habitRepository;
  private final GoalRepository goalRepository;
  private final HabitMapper habitMapper;
  private final UserInternalAPI userInternalAPI;

  @Override
  public List<HabitDTO> findAllHabitsForGoal(long goalId) {
    return habitMapper.toDtos(habitRepository.findAllByGoalId(goalId));
  }

  @Override
  @Transactional
  public HabitDTO addHabit(HabitDTO habit) {

    if (!userInternalAPI.existsById(habit.userId()))
      throw new EntityNotFoundException("User not exists");
    if (!goalRepository.existsById(habit.goalId()))
      throw new EntityNotFoundException("Goal not exists");

    var savedHabit = habitRepository.save(habitMapper.toEntity(habit));
    log.info(String.format("Added habit: %s", savedHabit));
    return habitMapper.toDto(savedHabit);
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
            .orElseThrow(() -> new EntityNotFoundException("Habit can't be found"));
    return habitMapper.toDto(habit);
  }

  @Override
  @Transactional
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

  @EventListener
  @Async
  void onUserDeleteEvent(UserDeletedEvent event) {
    log.info("OnUserDeleteEvent. User id: {}", event.getId());
    habitRepository.deleteAllByUserId(event.getId());
  }
}
