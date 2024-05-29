package net.szymonsawicki.net.habittracker.user;

public interface UserExternalAPI {
  UserDTO addUser(UserDTO user);

  UserDTO deleteWithRelatedData(long userId);

  UserDTO findByIdWithGoalsAndHabits(long id);
}
