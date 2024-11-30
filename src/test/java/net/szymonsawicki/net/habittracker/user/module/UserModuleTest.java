package net.szymonsawicki.net.habittracker.user.module;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityNotFoundException;
import net.szymonsawicki.net.habittracker.UserDeleteEvent;
import net.szymonsawicki.net.habittracker.user.repository.UserRepository;
import net.szymonsawicki.net.habittracker.user.service.UserService;
import net.szymonsawicki.net.habittracker.util.TestDataFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest(ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserModuleTest {

  @Autowired UserRepository userRepository;
  @Autowired private UserService userService;

  @BeforeEach
  public void loadTestData() {

    // creates 5 goals for 3 users

    userRepository.saveAll(TestDataFactory.createTestUserEntities());
  }

  @Test
  @Order(1)
  void shouldRemoveGoalOnUserDeletedEvent(Scenario scenario) {

    // user with id 1 have 2 goals

    var userToDelete = 1L;

    scenario
        .stimulate(() -> userService.deleteWithRelatedData(userToDelete))
        .andWaitForEventOfType(UserDeleteEvent.class)
        .matching(event -> event.getId().equals(userToDelete))
        .toArriveAndVerify(
            event ->
                assertThatThrownBy(() -> userService.findById(userToDelete))
                    .isInstanceOf(EntityNotFoundException.class));

    assertThat(userRepository.existsById(userToDelete)).isFalse();
  }
}
