package net.szymonsawicki.net.habittracker.user.mapper;

import java.util.List;
import net.szymonsawicki.net.habittracker.user.UserDTO;
import net.szymonsawicki.net.habittracker.user.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
  UserDTO toDto(UserEntity user);

  UserEntity toEntity(UserDTO user);

  List<UserDTO> toDtos(Iterable<UserEntity> users);

  List<UserEntity> toEntities(Iterable<UserDTO> users);
}
