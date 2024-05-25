package net.szymonsawicki.net.habittracker.gateway;

import net.szymonsawicki.net.habittracker.goal.GoalExternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitExternalAPI;
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

  @GetMapping("/{userId}")
  private UserDTO findUser(@PathVariable("userId") long userId) {
    return userExternalAPI.findByIdWithGoalsAndHabits(userId);
  }

  @DeleteMapping("/{userId}")
  private UserDTO deleteUser(@PathVariable("userId") long userId) {
    return userExternalAPI.deleteWithRelatedData(userId);
  }
}
