package net.szymonsawicki.net.habittracker.goal.module;

import static org.assertj.core.api.Assertions.*;

import net.szymonsawicki.net.habittracker.UserDeleteEvent;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.mapper.GoalMapper;
import net.szymonsawicki.net.habittracker.goal.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.goal.service.GoalService;
import net.szymonsawicki.net.habittracker.util.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest(
    value = ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GoalModuleTest {

  @Autowired private GoalRepository goalRepository;

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired GoalMapper goalMapper;

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
}
