package net.szymonsawicki.net.habittracker.usermanagement.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.events.UserCreatedEvent;
import net.szymonsawicki.net.habittracker.events.UserDeletedEvent;
import net.szymonsawicki.net.habittracker.usermanagement.UserDTO;
import net.szymonsawicki.net.habittracker.usermanagement.UserExternalAPI;
import net.szymonsawicki.net.habittracker.usermanagement.UserInternalAPI;
import net.szymonsawicki.net.habittracker.usermanagement.mapper.UserMapper;
import net.szymonsawicki.net.habittracker.usermanagement.repository.UserRepository;
import net.szymonsawicki.net.habittracker.usermanagement.service.exception.UserServiceException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserInternalAPI, UserExternalAPI {
  private final ApplicationEventPublisher events;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public List<UserDTO> findAllUsers() {
    return userMapper.toDtos(userRepository.findAll());
  }

  @Override
  @Transactional
  public UserDTO addUser(UserDTO user) {

    if (userRepository.existsByUsername(user.username())) {
      throw new UserServiceException("User with given username already exists");
    }

    var userEntity = userMapper.toEntity(user);
    var addedUser = userRepository.save(userEntity);

    events.publishEvent(new UserCreatedEvent(addedUser.getId(), user.userGoals()));

    log.info("Added user: {}", addedUser);

    var mappedUser = userMapper.toDto(userEntity);
    mappedUser.userGoals().addAll(user.userGoals());

    return mappedUser;
  }

  @Override
  public UserDTO findById(long userId) {
    var userFromDb =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User can't be found"));
    return userMapper.toDto(userFromDb);
  }

  @Override
  public boolean existsById(long userId) {
    if (!userRepository.existsById(userId)) {
      throw new EntityNotFoundException("User can't be found");
    }
    return true;
  }

  @Override
  @Transactional
  public long deleteWithRelatedData(long userId) {
    existsById(userId);
    events.publishEvent(new UserDeletedEvent(userId));
    userRepository.deleteById(userId);
    return userId;
  }

  @Override
  public List<UserDTO> addUsers(List<UserDTO> usersToAdd) {
    var addedUsers = userRepository.saveAll(userMapper.toEntities(usersToAdd));
    return userMapper.toDtos(addedUsers);
  }

  @Override
  public UserDTO findByUsername(String username) {
    return userMapper.toDto(userRepository.findByUsername(username));
  }
}
