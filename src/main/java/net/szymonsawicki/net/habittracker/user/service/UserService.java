package net.szymonsawicki.net.habittracker.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.szymonsawicki.net.habittracker.goal.GoalInternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerInternalApi;
import net.szymonsawicki.net.habittracker.user.UserDTO;
import net.szymonsawicki.net.habittracker.user.UserExternalAPI;
import net.szymonsawicki.net.habittracker.user.UserInternalAPI;
import net.szymonsawicki.net.habittracker.user.mapper.UserMapper;
import net.szymonsawicki.net.habittracker.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService implements UserInternalAPI, UserExternalAPI {
  private HabitInternalAPI habitInternalAPI;
  private GoalInternalAPI goalInternalAPI;
  private HabitTrackerInternalApi habitTrackerInternalApi;
  private UserRepository userRepository;
  private UserMapper userMapper;

  public UserService(
      HabitInternalAPI habitInternalAPI,
      GoalInternalAPI goalInternalAPI,
      HabitTrackerInternalApi habitTrackerInternalApi,
      UserRepository userRepository,
      UserMapper userMapper) {
    this.habitInternalAPI = habitInternalAPI;
    this.goalInternalAPI = goalInternalAPI;
    this.habitTrackerInternalApi = habitTrackerInternalApi;
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public UserDTO addUser(UserDTO user) {

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

    userFromDb.goals().addAll(goalInternalAPI.findGoalsForUser(userId));

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
  @Transactional
  public UserDTO deleteWithRelatedData(long userId) {
    var userFromDb = findById(userId);
    goalInternalAPI.deleteGoalsForUser(userId);
    habitInternalAPI.deleteHabitsForUser(userId);
    habitTrackerInternalApi.deleteTrackingsForUser(userId);
    return userFromDb;
  }
}
