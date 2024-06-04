package net.szymonsawicki.net.habittracker.habit.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.GoalExistsEvent;
import net.szymonsawicki.net.habittracker.HabitExistsEvent;
import net.szymonsawicki.net.habittracker.UserDeleteEvent;
import net.szymonsawicki.net.habittracker.UserExistsEvent;
import net.szymonsawicki.net.habittracker.habit.HabitDTO;
import net.szymonsawicki.net.habittracker.habit.HabitExternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.habit.mapper.HabitMapper;
import net.szymonsawicki.net.habittracker.habit.model.HabitEntity;
import net.szymonsawicki.net.habittracker.habit.repository.HabitRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class HabitService implements HabitExternalAPI, HabitInternalAPI {
  private final ApplicationEventPublisher eventPublisher;
  private final HabitRepository habitRepository;
  private final HabitMapper habitMapper;

  @Override
  public List<HabitDTO> findAllHabitsForGoal(long goalId) {
    return habitMapper.toDtos(habitRepository.findAllByGoalId(goalId));
  }

  @Override
  @Transactional
  public HabitDTO addHabit(HabitDTO habit) {

    eventPublisher.publishEvent(new UserExistsEvent(habit.userId()));
    eventPublisher.publishEvent(new GoalExistsEvent(habit.goalId()));

    var savedHabit = habitRepository.save(habitMapper.toEntity(habit));
    log.info(String.format("Added habit: %s", savedHabit));
    return habitMapper.toDto(savedHabit);
  }

  @Override
  public List<HabitDTO> findAllHabitsForUser(long userId) {
    eventPublisher.publishEvent(new UserExistsEvent(userId));
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
  void onHabitExistsEvent(HabitExistsEvent event) {
    log.info("OnHabitExistsEvent. Habit id: {}", event.getId());
    existsById(event.getId());
  }

  @EventListener
  void onUserDeleteEvent(UserDeleteEvent event) {
    log.info("OnUserDeleteEvent. User id: {}", event.getId());
    habitRepository.deleteAllByUserId(event.getId());
  }
}
