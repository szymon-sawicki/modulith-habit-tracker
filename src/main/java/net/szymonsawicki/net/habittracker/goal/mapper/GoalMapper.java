package net.szymonsawicki.net.habittracker.goal.mapper;

import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.model.GoalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GoalMapper {
  GoalDTO toDto(GoalEntity entity);

  GoalEntity toEntity(GoalDTO goalDTO);
}
