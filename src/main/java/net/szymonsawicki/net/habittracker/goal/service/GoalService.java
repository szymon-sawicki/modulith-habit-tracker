package net.szymonsawicki.net.habittracker.goal.service;

import jakarta.persistence.EntityNotFoundException;
import net.szymonsawicki.net.habittracker.goal.GoalDTO;
import net.szymonsawicki.net.habittracker.goal.GoalExternalAPI;
import net.szymonsawicki.net.habittracker.goal.GoalInternalAPI;
import net.szymonsawicki.net.habittracker.goal.mapper.GoalMapper;
import net.szymonsawicki.net.habittracker.goal.repository.GoalRepository;
import net.szymonsawicki.net.habittracker.habit.HabitInternalAPI;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService implements GoalInternalAPI, GoalExternalAPI {
    private GoalMapper goalMapper;
    private GoalRepository goalRepository;
    private HabitInternalAPI habitInternalAPI;

    @Override
    public GoalDTO findGoalWithHabits(long goalId) {
        var goal =
                goalRepository
                        .findById(goalId)
                        .orElseThrow(() -> new EntityNotFoundException("Goal can't be found"));

        var goalDto = goalMapper.toDto(goal);

        goalDto.habits().addAll(habitInternalAPI.findAllHabitsForGoal(goalId));

        return goalDto;
    }

    @Override
    public List<GoalDTO> findGoalsForUser(long userId) {

        return goalRepository.findByUserId(userId);
    }
}
