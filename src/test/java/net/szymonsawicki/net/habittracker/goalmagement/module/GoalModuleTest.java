package net.szymonsawicki.net.habittracker.goalmagement.module;

import static org.assertj.core.api.Assertions.*;

import net.szymonsawicki.net.habittracker.events.UserDeletedEvent;
import net.szymonsawicki.net.habittracker.goalmagement.GoalDTO;
import net.szymonsawicki.net.habittracker.goalmagement.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.goalmagement.service.GoalService;
import net.szymonsawicki.net.habittracker.util.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest(
    value = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES,
    module = "goal")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GoalModuleTest {

  @Autowired private GoalRepository goalRepository;

  @Autowired private GoalService goalService;

  @BeforeEach
  public void loadTestData() {

    // creates 5 goals for 3 users

    goalRepository.saveAll(TestDataFactory.createTestGoalEntities());
  }

  @Test
  @Order(1)
  void shouldRemoveGoalOnUserDeletedEvent(Scenario scenario) {

    // user with id 1 have 2 goals

    var userToDelete = 1L;

    scenario
        .publish(new UserDeletedEvent(userToDelete))
        .andWaitForStateChange(() -> goalRepository.count())
        .andVerify(
            result -> {
              assert result.intValue() == 3;
            });

    assertThat(goalRepository.findByUserId(userToDelete)).isEmpty();
  }

  @Test
  @Order(2)
  void shouldAddGoalOnAddGoal(Scenario scenario) {

    // given

    goalRepository.deleteAll();

    var goalUserId = 1L;

    var goalToAdd = new GoalDTO(null, goalUserId, "Test goal", "Test description");

    // then & when

    scenario
        .stimulate(() -> goalService.addGoal(goalToAdd))
        .andWaitForStateChange(() -> goalRepository.count())
        .andVerify(
            result -> {
              assertThat(goalRepository.findByUserId(1L).size()).isEqualTo(1);
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

    scenario
        .stimulate(() -> goalService.addGoal(goalToAdd))
        .andWaitForStateChange(() -> goalRepository.existsByName(testGoalName))
        .andVerify(
            result -> {
              assertThat(result).isFalse();
            });
  }
}
