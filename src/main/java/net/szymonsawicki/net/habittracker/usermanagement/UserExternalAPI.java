package net.szymonsawicki.net.habittracker.usermanagement;

import java.util.List;

public interface UserExternalAPI {
  List<UserDTO> findAllUsers();

  UserDTO addUser(UserDTO user);

  long deleteWithRelatedData(long userId);

  List<UserDTO> addUsers(List<UserDTO> usersToAdd);
}
