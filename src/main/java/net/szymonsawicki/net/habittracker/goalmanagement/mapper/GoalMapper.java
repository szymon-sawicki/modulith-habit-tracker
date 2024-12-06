package net.szymonsawicki.net.habittracker.goalmanagement.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.szymonsawicki.net.habittracker.goalmanagement.GoalDTO;
import net.szymonsawicki.net.habittracker.goalmanagement.model.GoalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GoalMapper {

  default GoalDTO toDto(GoalEntity goal) {
    return new GoalDTO(
        goal.getId(), goal.getUserId(), goal.getName(), goal.getDescription(), new ArrayList<>());
  }

  default List<GoalDTO> toDtos(List<GoalEntity> goals) {
    if (goals == null) {
      return new ArrayList<>();
    }
    return goals.stream().map(this::toDto).collect(Collectors.toCollection(ArrayList::new));
  }

  default GoalEntity toEntity(GoalDTO dto) {
    if (dto == null) {
      return null;
    }
    GoalEntity goal = new GoalEntity();
    goal.setId(dto.id());
    goal.setUserId(dto.userId());
    goal.setName(dto.name());
    goal.setDescription(dto.description());
    return goal;
  }

  List<GoalEntity> toEntities(List<GoalDTO> dtos);
}
