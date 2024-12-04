package net.szymonsawicki.net.habittracker.usermanagement;

public interface UserInternalAPI {
  UserDTO findById(long userId);

  boolean existsById(long userId);

  UserDTO findByUsername(String username);
}
