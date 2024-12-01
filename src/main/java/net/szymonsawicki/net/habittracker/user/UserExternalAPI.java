package net.szymonsawicki.net.habittracker.user;

import java.util.List;

public interface UserExternalAPI {
  List<UserDTO> findAllUsers();

  UserDTO addUser(UserDTO user);

  long deleteWithRelatedData(long userId);

  UserDTO findByIdWithGoalsAndHabits(long id);

  List<UserDTO> addUsers(List<UserDTO> usersToAdd);
}
