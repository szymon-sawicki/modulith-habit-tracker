package net.szymonsawicki.net.habittracker.tracker.mapper;

import java.util.List;
import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.model.HabitExecutionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HabitExecutionMapper {
  HabitExecutionDTO toDto(HabitExecutionEntity entity);

  HabitExecutionEntity toEntity(HabitExecutionDTO goalDTO);

  List<HabitExecutionDTO> toDtos(List<HabitExecutionEntity> entities);

  List<HabitExecutionEntity> toEntities(List<HabitExecutionDTO> dto);
}
