package net.szymonsawicki.net.habittracker.gateway;

import java.util.List;
import lombok.RequiredArgsConstructor;
import net.szymonsawicki.net.habittracker.goalmagement.*;
import net.szymonsawicki.net.habittracker.goalmagement.GoalDTO;
import net.szymonsawicki.net.habittracker.goalmagement.HabitDTO;
import net.szymonsawicki.net.habittracker.goalmagement.UserWithGoalsDTO;
import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerExternalApi;
import net.szymonsawicki.net.habittracker.tracker.UserTrackerDTO;
import net.szymonsawicki.net.habittracker.usermanagement.UserDTO;
import net.szymonsawicki.net.habittracker.usermanagement.UserExternalAPI;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HabitTrackerApi {
  private final GoalExternalAPI goalExternalAPI;
  private final HabitExternalAPI habitExternalAPI;
  private final HabitTrackerExternalApi habitTrackerExternalApi;
  private final UserExternalAPI userExternalAPI;

  @GetMapping("user/")
  public List<UserDTO> findAllUsers() {
    return userExternalAPI.findAllUsers();
  }

  @GetMapping("user/{userId}")
  public UserWithGoalsDTO findUser(@PathVariable("userId") long userId) {
    return goalExternalAPI.findUserWithGoals(userId);
  }

  @GetMapping("habit/user/{userId}")
  public List<HabitDTO> findAllHabitsForUser(@PathVariable("userId") long userId) {
    return habitExternalAPI.findAllHabitsForUser(userId);
  }

  @GetMapping("habit/goal/{goalId}")
  public List<HabitDTO> findAllHabitsForGoal(@PathVariable("goalId") long goalId) {
    return habitExternalAPI.findAllHabitsForGoal(goalId);
  }

  @DeleteMapping("user/{userId}")
  public long deleteUser(@PathVariable("userId") long userId) {
    return userExternalAPI.deleteWithRelatedData(userId);
  }

  @PostMapping("/user")
  public UserDTO addUser(@RequestBody UserDTO userDTO) {
    return userExternalAPI.addUser(userDTO);
  }

  @PostMapping("/habit")
  public HabitDTO addHabit(@RequestBody HabitDTO habit) {
    return habitExternalAPI.addHabit(habit);
  }

  @PostMapping("/goal")
  public GoalDTO addHabitExecution(@RequestBody GoalDTO goalDTO) {
    return goalExternalAPI.addGoal(goalDTO);
  }

  @PostMapping("/habit-execution")
  public HabitExecutionDTO addHabitExecution(@RequestBody HabitExecutionDTO habitExecutionDTO) {
    return habitTrackerExternalApi.addHabitExecution(habitExecutionDTO);
  }

  @GetMapping("habit-execution/habit/{habitId}")
  public List<HabitExecutionDTO> findAllHabitExecutionsForHabit(
      @PathVariable("habitId") long habitId) {
    return habitTrackerExternalApi.findAllExecutionsByHabitId(habitId);
  }

  @GetMapping("habit-execution/user/{userId}")
  public List<HabitExecutionDTO> findAllHabitExecutionsForUser(
      @PathVariable("userId") long userId) {
    return habitTrackerExternalApi.findAllExecutionsByHabitId(userId);
  }

  @GetMapping("habit-execution/tracker/{userId}")
  public UserTrackerDTO getHabitTrackerForUser(@PathVariable("userId") long userId) {
    return habitTrackerExternalApi.getUserTracker(userId);
  }
}
