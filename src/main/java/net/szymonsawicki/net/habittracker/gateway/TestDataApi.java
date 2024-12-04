package net.szymonsawicki.net.habittracker.gateway;

import java.util.ArrayList;
import java.util.List;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.GoalExternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitDTO;
import net.szymonsawicki.net.habittracker.habit.HabitExternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitPriority;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerExternalApi;
import net.szymonsawicki.net.habittracker.UserDTO;
import net.szymonsawicki.net.habittracker.usermanagement.UserExternalAPI;
import net.szymonsawicki.net.habittracker.usermanagement.UserType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-data")
public class TestDataApi {
  private GoalExternalAPI goalExternalAPI;
  private HabitExternalAPI habitExternalAPI;
  private HabitTrackerExternalApi habitTrackerExternalApi;
  private UserExternalAPI userExternalAPI;

  public TestDataApi(
      GoalExternalAPI goalExternalAPI,
      HabitExternalAPI habitExternalAPI,
      HabitTrackerExternalApi habitTrackerExternalApi,
      UserExternalAPI userExternalAPI) {
    this.goalExternalAPI = goalExternalAPI;
    this.habitExternalAPI = habitExternalAPI;
    this.habitTrackerExternalApi = habitTrackerExternalApi;
    this.userExternalAPI = userExternalAPI;
  }

  @PostMapping("/")
  public String createTestDataset() {

    var insertedUserId =
        userExternalAPI
            .addUser(new UserDTO(null, "TestName", "123456", UserType.USER, new ArrayList<>()))
            .id();
    var insertedGoalId =
        goalExternalAPI
            .addGoal(new GoalDTO(null, insertedUserId, "Longer life", "test description"))
            .id();
    var insertedHabit =
        habitExternalAPI.addHabit(
            new HabitDTO(
                null,
                insertedGoalId,
                insertedUserId,
                "Regular activity",
                "Daily sport activity",
                HabitPriority.HIGH));

    return "TEST DATA SUCCESFULY CREATED";
  }

  @PostMapping("/users/")
  public String createTestUsers(@RequestBody List<UserDTO> usersToAdd) {

    var insertedUserIds = userExternalAPI.addUsers(usersToAdd);

    return "TEST USER SUCCESFULLY CREATED. IDs : " + insertedUserIds;
  }
}
