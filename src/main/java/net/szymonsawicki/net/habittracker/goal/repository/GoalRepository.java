package net.szymonsawicki.net.habittracker.goal.repository;

import java.util.List;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.model.GoalEntity;
import org.springframework.data.repository.CrudRepository;

public interface GoalRepository extends CrudRepository<GoalEntity, Long> {

  List<GoalDTO> findByUserId(long userId);

  void deleteByUserId(long userId);
}
