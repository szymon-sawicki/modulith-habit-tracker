package net.szymonsawicki.net.habittracker.gateway;

import java.util.List;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.GoalExternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitDTO;
import net.szymonsawicki.net.habittracker.habit.HabitExternalAPI;
import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerExternalApi;
import net.szymonsawicki.net.habittracker.user.UserDTO;
import net.szymonsawicki.net.habittracker.user.UserExternalAPI;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class HabitTrackerApi {
  private GoalExternalAPI goalExternalAPI;
  private HabitExternalAPI habitExternalAPI;
  private HabitTrackerExternalApi habitTrackerExternalApi;
  private UserExternalAPI userExternalAPI;

  public HabitTrackerApi(
      GoalExternalAPI goalExternalAPI,
      HabitExternalAPI habitExternalAPI,
      HabitTrackerExternalApi habitTrackerExternalApi,
      UserExternalAPI userExternalAPI) {
    this.goalExternalAPI = goalExternalAPI;
    this.habitExternalAPI = habitExternalAPI;
    this.habitTrackerExternalApi = habitTrackerExternalApi;
    this.userExternalAPI = userExternalAPI;
  }

  @GetMapping("user/{userId}")
  public UserDTO findUser(@PathVariable("userId") long userId) {
    return userExternalAPI.findByIdWithGoalsAndHabits(userId);
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
  public UserDTO deleteUser(@PathVariable("userId") long userId) {
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
}
