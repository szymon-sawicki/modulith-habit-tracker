package net.szymonsawicki.net.habittracker.goal.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import net.szymonsawicki.net.habittracker.GoalExistsEvent;
import net.szymonsawicki.net.habittracker.UserDeleteEvent;
import net.szymonsawicki.net.habittracker.UserExistsEvent;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.mapper.GoalMapper;
import net.szymonsawicki.net.habittracker.goal.mapper.GoalMapperImpl;
import net.szymonsawicki.net.habittracker.goal.model.GoalEntity;
import net.szymonsawicki.net.habittracker.goal.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.goal.service.GoalService;
import net.szymonsawicki.net.habittracker.habit.HabitDTO;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.habit.HabitPriority;
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

  @Mock private GoalRepository goalRepository;

  @Mock private HabitInternalAPI habitInternalAPI;

  @InjectMocks private GoalService goalService;

  private final GoalMapper goalMapper = new GoalMapperImpl();

  private GoalEntity testGoalEntity;
  private GoalDTO testGoalDTO;
  private List<HabitDTO> testHabits;

  @BeforeEach
  void setUp() {
    testGoalEntity = new GoalEntity();
    testGoalEntity.setId(1L);
    testGoalEntity.setUserId(1L);
    testGoalEntity.setName("Test Goal");
    testGoalEntity.setDescription("Test Description");

    testHabits = List.of(new HabitDTO(1L, 1L, 1L, "Habit 1", "Description 1", HabitPriority.HIGH));

    testGoalDTO = new GoalDTO(1L, 1L, "Test Goal", "Test Description", testHabits);

    // Inject the actual mapper
    ReflectionTestUtils.setField(goalService, "goalMapper", goalMapper);
  }

  @Test
  void existsByGoalId_WhenGoalExists_ReturnsTrue() {
    when(goalRepository.existsById(1L)).thenReturn(true);

    assertTrue(goalService.existsByGoalId(1L));
    verify(goalRepository).existsById(1L);
  }

  @Test
  void existsByGoalId_WhenGoalDoesNotExist_ThrowsException() {
    when(goalRepository.existsById(1L)).thenReturn(false);

    assertThrows(EntityNotFoundException.class, () -> goalService.existsByGoalId(1L));
  }

  @Test
  void findGoalWithHabits_WhenGoalExists_ReturnsGoalWithHabits() {
    when(goalRepository.findById(1L)).thenReturn(Optional.of(testGoalEntity));
    when(habitInternalAPI.findAllHabitsForGoal(1L)).thenReturn(testHabits);

    GoalDTO result = goalService.findGoalWithHabits(1L);

    assertNotNull(result);
    assertEquals(testGoalEntity.getId(), result.id());
    assertEquals(testGoalEntity.getName(), result.name());
    assertEquals(testHabits.size(), result.habits().size());
    verify(habitInternalAPI).findAllHabitsForGoal(1L);
  }

  @Test
  void findGoalWithHabits_WhenGoalDoesNotExist_ThrowsException() {
    when(goalRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> goalService.findGoalWithHabits(1L));
  }

  @Test
  void findGoalsForUser_ReturnsGoalsWithHabits() {
    List<GoalEntity> goalEntities = List.of(testGoalEntity);
    when(goalRepository.findByUserId(1L)).thenReturn(goalEntities);
    when(habitInternalAPI.findAllHabitsForGoal(1L)).thenReturn(testHabits);

    List<GoalDTO> results = goalService.findGoalsForUser(1L);

    assertNotNull(results);
    assertEquals(1, results.size());
    assertEquals(testHabits.size(), results.get(0).habits().size());
    verify(eventPublisher).publishEvent(any(UserExistsEvent.class));
  }

  @Test
  void addGoal_WithoutHabits_SavesGoalSuccessfully() {
    GoalDTO inputGoalDTO = new GoalDTO(null, 1L, "Test Goal", "Test Description");
    when(goalRepository.save(any(GoalEntity.class))).thenReturn(testGoalEntity);

    GoalDTO result = goalService.addGoal(inputGoalDTO);

    assertNotNull(result);
    assertEquals(testGoalEntity.getId(), result.id());
    verify(eventPublisher).publishEvent(any(UserExistsEvent.class));
    verify(habitInternalAPI, never()).saveHabits(any());
  }

  @Test
  void addGoal_WithHabits_SavesGoalAndHabits() {
    when(goalRepository.save(any(GoalEntity.class))).thenReturn(testGoalEntity);
    when(habitInternalAPI.saveHabits(any())).thenReturn(testHabits);

    GoalDTO result = goalService.addGoal(testGoalDTO);

    assertNotNull(result);
    assertEquals(testGoalEntity.getId(), result.id());
    verify(eventPublisher).publishEvent(any(UserExistsEvent.class));
    verify(habitInternalAPI).saveHabits(any());
  }

  @Test
  void onUserDeleteEvent_DeletesUserGoals() {
    UserDeleteEvent event = new UserDeleteEvent(1L);

    goalService.onUserDeleteEvent(event);

    verify(goalRepository).deleteByUserId(1L);
  }

  @Test
  void onGoalExistsEvent_VerifiesGoalExists() {
    GoalExistsEvent event = new GoalExistsEvent(1L);
    when(goalRepository.existsById(1L)).thenReturn(true);

    goalService.onGoalExistsEvent(event);

    verify(goalRepository).existsById(1L);
  }
}
