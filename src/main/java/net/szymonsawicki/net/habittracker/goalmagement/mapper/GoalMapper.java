package net.szymonsawicki.net.habittracker.goalmagement.mapper;

import java.util.List;
import net.szymonsawicki.net.habittracker.goalmagement.GoalDTO;
import net.szymonsawicki.net.habittracker.goalmagement.model.GoalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GoalMapper {

  @Mapping(target = "habits", ignore = true)
  GoalDTO toDto(GoalEntity entity);

  GoalEntity toEntity(GoalDTO goalDTO);

  List<GoalDTO> toDtos(List<GoalEntity> entities);

  List<GoalEntity> toEntities(List<GoalDTO> dtos);
}