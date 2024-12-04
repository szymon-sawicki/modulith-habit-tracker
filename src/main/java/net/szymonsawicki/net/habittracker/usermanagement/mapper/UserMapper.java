package net.szymonsawicki.net.habittracker.usermanagement.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;
import net.szymonsawicki.net.habittracker.UserDTO;
import net.szymonsawicki.net.habittracker.usermanagement.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  default UserDTO toDto(UserEntity user) {
    return new UserDTO(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getUserType(),
        new ArrayList<>());
  }

  UserEntity toEntity(UserDTO user);

  default List<UserDTO> toDtos(Iterable<UserEntity> users) {
    return StreamSupport.stream(users.spliterator(), false).map(this::toDto).toList();
  }

  List<UserEntity> toEntities(Iterable<UserDTO> users);
}
