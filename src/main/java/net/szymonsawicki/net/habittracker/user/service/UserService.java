package net.szymonsawicki.net.habittracker.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserService implements UserInternalAPI, UserExternalAPI {
  private HabitInternalAPI habitInternalAPI;
  private GoalInternalAPI goalInternalAPI;
  private HabitTrackerInternalApi habitTrackerInternalApi;
  private UserRepository userRepository;
  private UserMapper userMapper;

  @Override
  public UserDTO add(UserDTO user) {
    var userEntity = userMapper.toEntity(user);
    return userMapper.toDto(userRepository.save(userEntity));
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
