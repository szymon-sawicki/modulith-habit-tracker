package net.szymonsawicki.net.habittracker.goal.repository;

import net.szymonsawicki.net.habittracker.goal.model.GoalEntity;
import org.springframework.data.repository.CrudRepository;

public interface GoalRepository extends CrudRepository<GoalEntity,Long> {
}
