package net.szymonsawicki.net.habittracker.tracker.repository;

import net.szymonsawicki.net.habittracker.tracker.model.HabitExecutionEntity;
import org.springframework.data.repository.CrudRepository;

public interface HabitExecutionRepository extends CrudRepository<HabitExecutionEntity,Long> {
}
