package net.szymonsawicki.net.habittracker.goalmanagement.module;

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import net.szymonsawicki.net.habittracker.events.UserCreatedEvent;
import net.szymonsawicki.net.habittracker.events.UserDeletedEvent;
import net.szymonsawicki.net.habittracker.goalmanagement.GoalDTO;
import net.szymonsawicki.net.habittracker.goalmanagement.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.goalmanagement.service.GoalService;
import net.szymonsawicki.net.habittracker.usermanagement.*;
import net.szymonsawicki.net.habittracker.util.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest(
    value = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES,
    module = "goalmanagement")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GoalModuleTest {

  @Autowired private GoalRepository goalRepository;

  @Autowired private UserExternalAPI userExternalAPI;
  @Autowired private UserTestApi userTestApi;

  @Autowired private GoalService goalService;

  @AfterEach
  public void cleanTestData() {

    // creates 5 goals for 3 users

    goalRepository.deleteAll();
    userTestApi.deleteAllUser();
  }

  @Test
  @Order(1)
  void shouldRemoveGoalOnUserDeletedEvent(Scenario scenario) {

    // Given

    goalRepository.saveAll(TestDataFactory.createTestGoalEntities());

    // user with id 1 have 2 goals
    var userToDelete = 1L;

    // When & Then

    scenario
        .publish(new UserDeletedEvent(userToDelete))
        .andWaitForStateChange(() -> goalRepository.count())
        .andVerify(
            result -> {
              assertThat(result.intValue()).isEqualTo(3);
              assertThat(goalRepository.findByUserId(userToDelete)).isEmpty();
            });
  }

  @Test
  @Order(2)
  void shouldAddGoalOnAddGoal(Scenario scenario) {

    // given

    goalRepository.deleteAll();

    var addedUser =
        userExternalAPI.addUser(
            new UserDTO(null, "TestUser", "TestPassword", UserType.USER, new ArrayList<>()));

    var goalToAdd = new GoalDTO(null, addedUser.id(), "Test goal", "Test description");

    // then & when

    scenario
        .stimulate(() -> goalService.addGoal(goalToAdd))
        .andWaitForStateChange(() -> goalRepository.findByUserId(addedUser.id()))
        .andVerify(
            result -> {
              assertThat(result.size()).isEqualTo(1);
            });
  }

  @Test
  @Order(3)
  void shouldThrowExceptionWhenUserNotExists(Scenario scenario) {

    // given

    var notExistingUserId = 999L;
    var testGoalName = "Test goal not existing user";

    var goalToAdd = new GoalDTO(null, notExistingUserId, testGoalName, "Test description");

    // then & when

    assertThatThrownBy(() -> goalService.addGoal(goalToAdd))
        .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @Order(4)
  void shouldCreateGoalsOnCreateUserDtoEvent(Scenario scenario) {

    // given

    var goalsToCreate = List.of("Running faster", "Eating less");

    var addedUserId =
        userExternalAPI
            .addUser(
                new UserDTO(null, "TestUser", "TestPassword", UserType.USER, new ArrayList<>()))
            .id();

    var userCreatedEvent = new UserCreatedEvent(addedUserId, goalsToCreate);

    // then & when

    scenario
        .publish(userCreatedEvent)
        .forEventOfType(UserCreatedEvent.class)
        .matching(event -> event.getId().equals(addedUserId))
        .toArriveAndVerify(
            result -> {
              assertThat(
                      goalService.findGoalsForUser(addedUserId).stream()
                          .map(GoalDTO::name)
                          .toList())
                  .containsAll(goalsToCreate);
            });
  }
}
