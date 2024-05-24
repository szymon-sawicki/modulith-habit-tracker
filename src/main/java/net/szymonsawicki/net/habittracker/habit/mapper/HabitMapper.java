package net.szymonsawicki.net.habittracker.habit.mapper;

import net.szymonsawicki.net.habittracker.habit.HabitDTO;
import net.szymonsawicki.net.habittracker.habit.model.HabitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HabitMapper {
  HabitDTO toDto(HabitEntity entity);

  HabitEntity toEntity(HabitDTO goalDTO);
}
