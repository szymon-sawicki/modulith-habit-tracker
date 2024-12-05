package net.szymonsawicki.net.habittracker.goalmagement.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import net.szymonsawicki.net.habittracker.events.UserDeletedEvent;
import net.szymonsawicki.net.habittracker.goalmagement.GoalDTO;
import net.szymonsawicki.net.habittracker.goalmagement.HabitDTO;
import net.szymonsawicki.net.habittracker.goalmagement.HabitInternalAPI;
import net.szymonsawicki.net.habittracker.goalmagement.HabitPriority;
import net.szymonsawicki.net.habittracker.goalmagement.mapper.GoalMapper;
import net.szymonsawicki.net.habittracker.goalmagement.mapper.GoalMapperImpl;
import net.szymonsawicki.net.habittracker.goalmagement.model.GoalEntity;
import net.szymonsawicki.net.habittracker.goalmagement.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.goalmagement.service.GoalService;
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
  void shouldCheckIfGoalExistsAndReturnTrue() {
    when(goalRepository.existsById(1L)).thenReturn(true);

    assertTrue(goalService.existsByGoalId(1L));
    verify(goalRepository).existsById(1L);
  }

  @Test
  void shouldCheckIfGoalExistsAndThrowException() {
    when(goalRepository.existsById(1L)).thenReturn(false);

    assertThrows(EntityNotFoundException.class, () -> goalService.existsByGoalId(1L));
  }

  @Test
  void shouldFindAndReturnGoalWithHabitsWhenGoalExists() {
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
  void shouldFindGoalWithHabitsWhenGoalNotExists() {
    when(goalRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> goalService.findGoalWithHabits(1L));
  }

  /*  @Test    TODO fix
    void shouldFindGoalForUser() {
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
    void shouldAddGoalWithoutHabitsWithSuccess() {
      GoalDTO inputGoalDTO = new GoalDTO(null, 1L, "Test Goal", "Test Description");
      when(goalRepository.save(any(GoalEntity.class))).thenReturn(testGoalEntity);

      GoalDTO result = goalService.addGoal(inputGoalDTO);

      assertNotNull(result);
      assertEquals(testGoalEntity.getId(), result.id());
      verify(eventPublisher).publishEvent(any(UserExistsEvent.class));
      verify(habitInternalAPI, never()).saveHabits(any());
    }

    @Test
    void shouldAddGoalWithHabitsWithSuccess() {
      when(goalRepository.save(any(GoalEntity.class))).thenReturn(testGoalEntity);
      when(habitInternalAPI.saveHabits(any())).thenReturn(testHabits);

      GoalDTO result = goalService.addGoal(testGoalDTO);

      assertNotNull(result);
      assertEquals(testGoalEntity.getId(), result.id());
      verify(eventPublisher).publishEvent(any(UserExistsEvent.class));
      verify(habitInternalAPI).saveHabits(any());
    }
  */
  @Test
  void shouldDeleteUserGoalWhenUserDeletedEvent() {
    UserDeletedEvent event = new UserDeletedEvent(1L);

    goalService.onUserDeleteEvent(event);

    verify(goalRepository).deleteByUserId(1L);
  }
  /*
  @Test TODO fix
  void shouldVerifyGoalExistenceWithSuccess() {
    GoalExistsEvent event = new GoalExistsEvent(1L);
    when(goalRepository.existsById(1L)).thenReturn(true);

    goalService.onGoalExistsEvent(event);

    verify(goalRepository).existsById(1L);
  }*/
}
