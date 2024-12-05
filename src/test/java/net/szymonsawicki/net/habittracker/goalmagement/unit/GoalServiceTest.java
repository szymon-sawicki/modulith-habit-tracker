package net.szymonsawicki.net.habittracker.goalmagement.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.szymonsawicki.net.habittracker.events.UserCreatedEvent;
import net.szymonsawicki.net.habittracker.events.UserDeletedEvent;
import net.szymonsawicki.net.habittracker.goalmagement.*;
import net.szymonsawicki.net.habittracker.goalmagement.mapper.GoalMapper;
import net.szymonsawicki.net.habittracker.goalmagement.model.GoalEntity;
import net.szymonsawicki.net.habittracker.goalmagement.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.goalmagement.service.GoalService;
import net.szymonsawicki.net.habittracker.usermanagement.UserDTO;
import net.szymonsawicki.net.habittracker.usermanagement.UserInternalAPI;
import net.szymonsawicki.net.habittracker.usermanagement.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

  @Mock private ApplicationEventPublisher eventPublisher;

  @Mock private GoalMapper goalMapper;

  @Mock private GoalRepository goalRepository;

  @Mock private HabitInternalAPI habitInternalAPI;

  @Mock private UserInternalAPI userInternalAPI;

  @InjectMocks private GoalService goalService;

  private GoalEntity testGoalEntity;
  private List<HabitDTO> testHabits;
  private GoalDTO testGoalDTO;

  @BeforeEach
  void setUp() {
    testGoalEntity = new GoalEntity();
    testGoalEntity.setId(1L);
    testGoalEntity.setUserId(1L);
    testGoalEntity.setName("Test Goal");
    testGoalEntity.setDescription("Test Description");

    testHabits = List.of(new HabitDTO(1L, 1L, 1L, "Habit 1", "Description 1", HabitPriority.HIGH));
    testGoalDTO = new GoalDTO(1L, 1L, "Test Goal", "Test Description", testHabits);

    ReflectionTestUtils.setField(goalService, "goalMapper", goalMapper);
  }

  @Test
  void shouldCheckIfGoalExistsAndReturnTrue() {
    // Given
    when(goalRepository.existsById(1L)).thenReturn(true);

    // When
    boolean result = goalService.existsByGoalId(1L);

    // Then
    assertTrue(result);
    verify(goalRepository).existsById(1L);
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenGoalDoesNotExist() {
    // Given
    when(goalRepository.existsById(1L)).thenReturn(false);

    // When & Then
    assertThrows(EntityNotFoundException.class, () -> goalService.existsByGoalId(1L));
  }

  @Test
  void shouldFindUserWithGoalsSuccessfully() {
    // Given
    var user = new UserDTO(1L, "testUser", "testPassword", UserType.USER, new ArrayList<>());
    var goals = List.of(testGoalEntity);
    when(userInternalAPI.findById(1L)).thenReturn(user);
    when(goalRepository.findByUserId(1L)).thenReturn(goals);
    when(goalMapper.toDtos(goals)).thenReturn(List.of(testGoalDTO));

    // When
    UserWithGoalsDTO result = goalService.findUserWithGoals(1L);

    // Then
    assertNotNull(result);
    assertEquals(user.id(), result.id());
    assertEquals(user.username(), result.username());
    assertFalse(result.userGoals().isEmpty());
  }

  @Test
  void shouldHandleUserCreatedEventAndCreateGoals() {
    // Given
    var goalNames = List.of("Goal 1", "Goal 2");
    var userCreatedEvent = new UserCreatedEvent(1L, goalNames);
    when(goalRepository.saveAll(any())).thenReturn(List.of(testGoalEntity));

    // When
    goalService.onUserCreatedEvent(userCreatedEvent);

    // Then
    verify(goalRepository).saveAll(any());
  }

  @Test
  void shouldFindGoalWithHabitsSuccessfully() {
    // Given
    when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoalEntity));
    when(goalMapper.toDto(testGoalEntity)).thenReturn(testGoalDTO);
    when(habitInternalAPI.findAllHabitsForGoal(1L)).thenReturn(testHabits);

    // When
    GoalDTO result = goalService.findGoalWithHabits(1L);

    // Then
    assertNotNull(result);
    assertEquals(testGoalDTO.id(), result.id());
    assertFalse(result.habits().isEmpty());
  }

  @Test
  void shouldThrowEntityNotFoundExceptionWhenFindingNonExistentGoal() {
    // Given
    when(goalRepository.findById(1L)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(EntityNotFoundException.class, () -> goalService.findGoalWithHabits(1L));
  }

  @Test
  void shouldAddGoalSuccessfully() {
    // Given
    when(userInternalAPI.existsById(1L)).thenReturn(true);
    when(goalMapper.toEntity(testGoalDTO)).thenReturn(testGoalEntity);
    when(goalRepository.save(any(GoalEntity.class))).thenReturn(testGoalEntity);
    when(goalMapper.toDto(testGoalEntity)).thenReturn(testGoalDTO);
    when(habitInternalAPI.saveHabits(any())).thenReturn(testHabits);

    // When
    GoalDTO result = goalService.addGoal(testGoalDTO);

    // Then
    assertNotNull(result);
    assertEquals(testGoalDTO.id(), result.id());
    verify(goalRepository).save(any(GoalEntity.class));
    verify(habitInternalAPI).saveHabits(any());
  }

  @Test
  void shouldHandleUserDeleteEventAndDeleteGoals() {
    // Given
    var userDeletedEvent = new UserDeletedEvent(1L);

    // When
    goalService.onUserDeleteEvent(userDeletedEvent);

    // Then
    verify(goalRepository).deleteByUserId(1L);
  }

  @Test
  void shouldFindGoalsForUserSuccessfully() {
    // Given
    when(userInternalAPI.existsById(1L)).thenReturn(true);
    when(goalRepository.findByUserId(1L)).thenReturn(List.of(testGoalEntity));
    when(goalMapper.toDtos(any())).thenReturn(List.of(testGoalDTO));
    when(habitInternalAPI.findAllHabitsForGoal(1L)).thenReturn(testHabits);

    // When
    List<GoalDTO> result = goalService.findGoalsForUser(1L);

    // Then
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    verify(habitInternalAPI).findAllHabitsForGoal(1L);
  }
}
