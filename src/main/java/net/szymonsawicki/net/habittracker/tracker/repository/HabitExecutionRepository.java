package net.szymonsawicki.net.habittracker.tracker.repository;

import java.util.List;
import net.szymonsawicki.net.habittracker.tracker.model.HabitExecutionEntity;
import org.springframework.data.repository.CrudRepository;

public interface HabitExecutionRepository extends CrudRepository<HabitExecutionEntity, Long> {
  List<HabitExecutionEntity> findAllByHabitId(long habitId);

  List<HabitExecutionEntity> findAllByUserId(long userId);

  void deleteAllByUserId(long userId);
}
