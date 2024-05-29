package net.szymonsawicki.net.habittracker.habit.repository;

import java.util.List;
import net.szymonsawicki.net.habittracker.habit.model.HabitEntity;
import org.springframework.data.repository.CrudRepository;

public interface HabitRepository extends CrudRepository<HabitEntity, Long> {
  void deleteAllByUserId(long userId);

  List<HabitEntity> findAllByGoalId(long goalId);
}
