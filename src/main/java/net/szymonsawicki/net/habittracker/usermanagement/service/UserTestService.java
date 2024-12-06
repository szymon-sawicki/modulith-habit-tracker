package net.szymonsawicki.net.habittracker.usermanagement.service;

import lombok.RequiredArgsConstructor;
import net.szymonsawicki.net.habittracker.usermanagement.UserTestApi;
import net.szymonsawicki.net.habittracker.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserTestService implements UserTestApi {

  private final UserRepository userRepository;

  @Override
  public void deleteAllUser() {
    userRepository.deleteAll();
  }
}
