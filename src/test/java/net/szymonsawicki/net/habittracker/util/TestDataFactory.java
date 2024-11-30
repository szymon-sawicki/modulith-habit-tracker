package net.szymonsawicki.net.habittracker.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.szymonsawicki.net.habittracker.goal.model.GoalEntity;
import net.szymonsawicki.net.habittracker.habit.model.HabitEntity;
import net.szymonsawicki.net.habittracker.tracker.model.HabitExecutionEntity;
import net.szymonsawicki.net.habittracker.user.UserType;
import net.szymonsawicki.net.habittracker.user.model.UserEntity;

public class TestDataFactory {

  public static GoalEntity createGoalEntity(Long userId, String name, String description) {
    GoalEntity goal = new GoalEntity();
    goal.setUserId(userId);
    goal.setName(name);
    goal.setDescription(description);
    return goal;
  }

  public static List<GoalEntity> createTestGoalEntities() {
    return List.of(
        createGoalEntity(1L, "Morning Exercise", "30 minutes of cardio every morning"),
        createGoalEntity(1L, "Reading", "Read 20 pages of a book daily"),
        createGoalEntity(2L, "Learn Programming", "Study Java for 1 hour"),
        createGoalEntity(2L, "Meditation", "15 minutes of mindfulness"),
        createGoalEntity(3L, "Healthy Diet", "Eat more vegetables and fruits"));
  }

  public static List<GoalEntity> createTestGoalEntitiesForUser(Long userId, int count) {
    return IntStream.range(0, count)
        .mapToObj(
            i ->
                createGoalEntity(
                    userId,
                    String.format("Goal %d for User %d", i + 1, userId),
                    String.format("Description for goal %d", i + 1)))
        .collect(Collectors.toList());
  }

  public static HabitEntity createHabitEntity(
      Long goalId, Long userId, String name, String description) {
    HabitEntity habit = new HabitEntity();
    habit.setGoalId(goalId);
    habit.setUserId(userId);
    habit.setName(name);
    habit.setDescription(description);
    return habit;
  }

  public static List<HabitEntity> createTestHabitEntities() {
    return List.of(
        createHabitEntity(1L, 1L, "Morning Exercise", "30 minutes workout"),
        createHabitEntity(1L, 1L, "Evening Reading", "Read technical books"),
        createHabitEntity(2L, 2L, "Meditation", "Daily mindfulness practice"),
        createHabitEntity(2L, 2L, "Coding Practice", "Algorithm challenges"),
        createHabitEntity(3L, 3L, "Healthy Eating", "Track calories intake"));
  }

  public static UserEntity createUserEntity(String username, String password, UserType userType) {
    UserEntity user = new UserEntity();
    user.setUsername(username);
    user.setPassword(password);
    user.setUserType(userType);
    return user;
  }

  public static List<UserEntity> createTestUserEntities() {
    return List.of(
        createUserEntity("john_doe", "password123", UserType.USER),
        createUserEntity("jane_smith", "password456", UserType.USER),
        createUserEntity("admin_user", "admin123", UserType.ADMIN),
        createUserEntity("test_user", "test123", UserType.ADMIN),
        createUserEntity("premium_user", "premium123", UserType.ADMIN));
  }

  public static HabitExecutionEntity createHabitExecutionEntity(
      Long habitId,
      Long userId,
      Integer durationMins,
      String comment,
      LocalDate executionDate,
      LocalTime executionTime) {
    HabitExecutionEntity execution = new HabitExecutionEntity();
    execution.setHabitId(habitId);
    execution.setUserId(userId);
    execution.setDurationMins(durationMins);
    execution.setComment(comment);
    execution.setExecutionDate(executionDate);
    execution.setExecutionTime(executionTime);
    return execution;
  }

  public static List<HabitExecutionEntity> createTestHabitExecutionEntities() {
    LocalDate today = LocalDate.now();
    return List.of(
        createHabitExecutionEntity(1L, 1L, 30, "Great morning workout", today, LocalTime.of(7, 0)),
        createHabitExecutionEntity(
            1L, 1L, 45, "Extended evening session", today, LocalTime.of(18, 0)),
        createHabitExecutionEntity(2L, 2L, 20, "Peaceful meditation", today, LocalTime.of(8, 0)),
        createHabitExecutionEntity(
            2L, 2L, 60, "Productive coding session", today, LocalTime.of(14, 0)),
        createHabitExecutionEntity(
            3L, 3L, 15, "Quick mindfulness break", today, LocalTime.of(12, 0)));
  }
}
