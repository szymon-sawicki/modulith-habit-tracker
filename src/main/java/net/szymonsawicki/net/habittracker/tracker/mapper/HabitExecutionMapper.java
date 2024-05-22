package net.szymonsawicki.net.habittracker.tracker.mapper;

import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.model.HabitExecutionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HabitExecutionMapper {
    HabitExecutionDTO toDto(HabitExecutionEntity entity);
    HabitExecutionEntity toEntity(HabitExecutionDTO goalDTO);
}
