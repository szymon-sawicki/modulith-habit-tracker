package net.szymonsawicki.net.habittracker.habit.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.habit.HabitDTO;
import net.szymonsawicki.net.habittracker.habit.HabitExternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.habit.mapper.HabitMapper;
import net.szymonsawicki.net.habittracker.habit.repository.HabitRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HabitService implements HabitExternalAPI, HabitInternalAPI {
  private HabitRepository habitRepository;
  private HabitMapper habitMapper;

  public HabitService(HabitRepository habitRepository, HabitMapper habitMapper) {
    this.habitRepository = habitRepository;
    this.habitMapper = habitMapper;
  }

  @Override
  public HabitDTO addHabit(HabitDTO habit) {
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
    return List.of();
  }
}
