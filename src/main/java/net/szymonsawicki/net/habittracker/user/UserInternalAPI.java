package net.szymonsawicki.net.habittracker.user;

public interface UserInternalAPI {
  UserDTO findById(long userId);

  boolean existsById(long userId);

  UserDTO findByUsername(String username);
}
