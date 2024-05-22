package net.szymonsawicki.net.habittracker.user;

import net.szymonsawicki.net.habittracker.user.type.UserType;

public record UserDTO(Long id, String username, String password, UserType userType) {

}
