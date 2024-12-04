package net.szymonsawicki.net.habittracker.goal.repository;

import java.util.List;
import net.szymonsawicki.net.habittracker.goal.model.GoalEntity;
import org.springframework.data.repository.CrudRepository;

public interface GoalRepository extends CrudRepository<GoalEntity, Long> {
  List<GoalEntity> findByUserId(long userId);

  boolean existsByName(String goalName);

  void deleteByUserId(long userId);
}
