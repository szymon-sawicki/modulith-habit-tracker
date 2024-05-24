package net.szymonsawicki.net.habittracker.habit.repository;

import net.szymonsawicki.net.habittracker.habit.model.HabitEntity;
import org.springframework.data.repository.CrudRepository;

public interface HabitRepository extends CrudRepository<HabitEntity, Long> {}
