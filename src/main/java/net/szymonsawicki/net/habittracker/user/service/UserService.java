package net.szymonsawicki.net.habittracker.user.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.UserDeleteEvent;
import net.szymonsawicki.net.habittracker.UserExistsEvent;
import net.szymonsawicki.net.habittracker.goal.GoalInternalAPI;
import net.szymonsawicki.net.habittracker.user.UserDTO;
import net.szymonsawicki.net.habittracker.user.UserExternalAPI;
import net.szymonsawicki.net.habittracker.user.UserInternalAPI;
import net.szymonsawicki.net.habittracker.user.mapper.UserMapper;
import net.szymonsawicki.net.habittracker.user.repository.UserRepository;
import net.szymonsawicki.net.habittracker.user.service.exception.UserServiceException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserInternalAPI, UserExternalAPI {
  private final ApplicationEventPublisher events;
  private final GoalInternalAPI goalInternalAPI;
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
    log.info(String.format("Added user: %s", addedUser));

    return userMapper.toDto(userEntity);
  }

  @Override
  @Transactional
  public UserDTO findByIdWithGoalsAndHabits(long userId) {
    var userFromDb =
        userMapper.toDto(
            userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User can't be found")));

    var goalsForUser = goalInternalAPI.findGoalsForUser(userId);

    if (!goalsForUser.isEmpty()) {
      userFromDb.goals().addAll(goalsForUser);
    }

    return userFromDb;
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

  @EventListener
  @Async
  void onExistsUserEvent(UserExistsEvent event) {
    log.info("OnUserExistsEvent. User id: {}", event.getId());
    existsById(event.getId());
  }

  @Override
  @Transactional
  public long deleteWithRelatedData(long userId) {
    existsById(userId);
    events.publishEvent(new UserDeleteEvent(userId));
    userRepository.deleteById(userId);
    return userId;
  }

  @Override
  public List<UserDTO> addUsers(List<UserDTO> usersToAdd) {
    var addedUsers = userRepository.saveAll(userMapper.toEntities(usersToAdd));
    return userMapper.toDtos(addedUsers);
  }
}
