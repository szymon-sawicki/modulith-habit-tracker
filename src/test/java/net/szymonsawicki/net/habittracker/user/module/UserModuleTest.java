package net.szymonsawicki.net.habittracker.user.module;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import net.szymonsawicki.net.habittracker.UserDeleteEvent;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.GoalInternalAPI;
import net.szymonsawicki.net.habittracker.user.UserDTO;
import net.szymonsawicki.net.habittracker.user.UserType;
import net.szymonsawicki.net.habittracker.user.repository.UserRepository;
import net.szymonsawicki.net.habittracker.user.service.UserService;
import net.szymonsawicki.net.habittracker.user.service.exception.UserServiceException;
import net.szymonsawicki.net.habittracker.util.TestDataFactory;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest(ApplicationModuleTest.BootstrapMode.ALL_DEPENDENCIES)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserModuleTest {

  @Autowired UserRepository userRepository;
  @Autowired private UserService userService;

  @Mock GoalInternalAPI goalInternalAPI;

  @AfterEach
  public void loadTestData() {

    userRepository.deleteAll();
  }

  @Test
  @Order(1)
  void shouldRemoveUserOnUserDeletedEvent(Scenario scenario) {

    userRepository.saveAll(TestDataFactory.createTestUserEntities());

    // user with id 1 have 2 goals
    var userToDelete = 1L;

    scenario
        .stimulate(() -> userService.deleteWithRelatedData(userToDelete))
        .andWaitForEventOfType(UserDeleteEvent.class)
        .matching(event -> event.getId().equals(userToDelete))
        .toArriveAndVerify(
            event ->
                assertThatThrownBy(() -> userService.findById(event.getId()))
                    .isInstanceOf(EntityNotFoundException.class));

    assertThat(userRepository.existsById(userToDelete)).isFalse();
  }

  @Test
  @Order(2)
  void shouldAddNewUser(Scenario scenario) {
    // given
    String testUsername = "testUser";
    UserDTO newUser = new UserDTO(null, testUsername, "Test User", UserType.USER, null);

    // when & then
    scenario
        .stimulate(() -> userService.addUser(newUser))
        .andWaitForStateChange(() -> userService.findByUsername(testUsername))
        .andVerify(
            savedUser -> {
              assertThat(savedUser.username()).isEqualTo(testUsername);
            });
  }

  @Test
  @Order(3)
  void shouldThrowExceptionWhenAddingDuplicateUser() {
    // given
    UserDTO user =
        new UserDTO(null, "existingUser", "Existing User", UserType.USER, new ArrayList<>());
    userService.addUser(user);

    // when & then
    assertThatThrownBy(() -> userService.addUser(user)).isInstanceOf(UserServiceException.class);
  }

  @Test
  void shouldFindUserWithGoalsAndHabits(Scenario scenario) {
    // given
    UserDTO newUser =
        new UserDTO(null, "userWithGoals", "User With Goals", UserType.USER, new ArrayList<>());
    UserDTO savedUser = userService.addUser(newUser);
    List<GoalDTO> mockGoals = TestDataFactory.createTestGoalDtosForUser(savedUser.id(), 3);

    // Use doReturn instead of when
    when(goalInternalAPI.findGoalsForUser(savedUser.id()))
        .thenReturn(TestDataFactory.createTestGoalDtosForUser(savedUser.id(), 3));

    // when
    var resultingUser = userService.findByIdWithGoalsAndHabits(savedUser.id());

    // then
    assertThat(resultingUser.id()).isEqualTo(savedUser.id());
    assertThat(resultingUser.goals()).hasSize(3);
    assertThat(resultingUser.goals()).containsAll(mockGoals);
  }
}
