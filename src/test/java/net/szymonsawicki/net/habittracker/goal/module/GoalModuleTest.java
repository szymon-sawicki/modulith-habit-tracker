package net.szymonsawicki.net.habittracker.goal.module;

import static org.assertj.core.api.Assertions.*;

import net.szymonsawicki.net.habittracker.UserDeleteEvent;
import net.szymonsawicki.net.habittracker.goal.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.goal.service.GoalService;
import net.szymonsawicki.net.habittracker.util.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest(ApplicationModuleTest.BootstrapMode.DIRECT_DEPENDENCIES)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GoalModuleTest {

  @Autowired GoalRepository goalRepository;
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
        .publish(new UserDeleteEvent(userToDelete))
        .andWaitForStateChange(() -> goalRepository.count())
        .andVerify(
            result -> {
              assert result.intValue() == 3;
            });

    assertThat(goalRepository.findByUserId(userToDelete)).isEmpty();
  }
}
