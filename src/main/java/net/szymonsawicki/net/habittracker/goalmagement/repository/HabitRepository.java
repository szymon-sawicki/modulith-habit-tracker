package net.szymonsawicki.net.habittracker.goalmagement.repository;

import java.util.List;
import net.szymonsawicki.net.habittracker.goalmagement.model.HabitEntity;
import org.springframework.data.repository.CrudRepository;

public interface HabitRepository extends CrudRepository<HabitEntity, Long> {
  void deleteAllByUserId(long userId);

  List<HabitEntity> findAllByUserId(long userId);

  List<HabitEntity> findAllByGoalId(long goalId);
}
