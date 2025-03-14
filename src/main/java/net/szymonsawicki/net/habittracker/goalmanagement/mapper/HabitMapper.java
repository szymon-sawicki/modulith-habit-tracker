package net.szymonsawicki.net.habittracker.goalmanagement.mapper;

import java.util.List;
import net.szymonsawicki.net.habittracker.goalmanagement.HabitDTO;
import net.szymonsawicki.net.habittracker.goalmanagement.model.HabitEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HabitMapper {

  @Mapping(target = "withGoalId", ignore = true)
  @Mapping(target = "priority", ignore = true)
  HabitDTO toDto(HabitEntity entity);

  HabitEntity toEntity(HabitDTO goalDTO);

  List<HabitDTO> toDtos(List<HabitEntity> entities);

  List<HabitEntity> toEntities(List<HabitDTO> dtos);
}
