package net.szymonsawicki.net.habittracker.usermanagement;

import net.szymonsawicki.net.habittracker.UserDTO;

public interface UserInternalAPI {
  UserDTO findById(long userId);

  boolean existsById(long userId);

  UserDTO findByUsername(String username);
}
