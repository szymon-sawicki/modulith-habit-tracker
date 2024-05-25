package net.szymonsawicki.net.habittracker.habit.service;

import net.szymonsawicki.net.habittracker.habit.HabitDTO;
import net.szymonsawicki.net.habittracker.habit.HabitExternalAPI;
import net.szymonsawicki.net.habittracker.habit.mapper.HabitMapper;
import net.szymonsawicki.net.habittracker.habit.repository.HabitRepository;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerInternalApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabitService implements HabitExternalAPI, HabitTrackerInternalApi {
  private HabitRepository habitRepository;
  private HabitMapper habitMapper;

  @Override
  public HabitDTO addHabit(HabitDTO habit) {
    var savedHabit = habitRepository.save(habitMapper.toEntity(habit));
    return habitMapper.toDto(savedHabit);
  }

  @Override
  @Transactional
  public void deleteTrackingsForUser(long userId) {
    habitRepository.deleteAllByUserId(userId);
  }
}
