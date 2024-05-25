package net.szymonsawicki.net.habittracker.user;

public interface UserExternalAPI {
  UserDTO add(UserDTO user);

  UserDTO deleteWithRelatedData(long userId);

  UserDTO findByIdWithGoalsAndHabits(long id);
}
