package net.szymonsawicki.net.habittracker.user;

public interface UserExternalAPI {
    UserDTO add(UserDTO user);
    UserDTO findById(long id);
}
