package net.szymonsawicki.net.habittracker.goal.repository;

import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.model.GoalEntity;
import net.szymonsawicki.net.habittracker.user.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GoalRepository extends CrudRepository<GoalEntity, Long> {

    List<GoalDTO> findByUserId(long userId);
    void deleteByUserId(long userId);


}
