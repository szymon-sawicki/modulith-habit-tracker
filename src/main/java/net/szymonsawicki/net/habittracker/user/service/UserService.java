package net.szymonsawicki.net.habittracker.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.szymonsawicki.net.habittracker.goal.GoalInternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.user.UserDTO;
import net.szymonsawicki.net.habittracker.user.UserExternalAPI;
import net.szymonsawicki.net.habittracker.user.UserInternalAPI;
import net.szymonsawicki.net.habittracker.user.mapper.UserMapper;
import net.szymonsawicki.net.habittracker.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserInternalAPI, UserExternalAPI {
  private HabitInternalAPI habitInternalAPI;
  private GoalInternalAPI goalInternalAPI;
  private UserRepository userRepository;
  private UserMapper userMapper;

  @Override
  public UserDTO add(UserDTO user) {
    var userEntity = userMapper.toEntity(user);
    return userMapper.toDto(userRepository.save(userEntity));
  }

  @Override
  public UserDTO findByIdWithGoalsAndHabits(long userId) {
    var user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User can't be found"));
    var userGoals = goalInternalAPI.findGoalsForUser(userId);
    return null;
  }

  @Override
  public UserDTO findById(long userId) {
    return null;
  }
}
