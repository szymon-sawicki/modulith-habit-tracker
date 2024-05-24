package net.szymonsawicki.net.habittracker.tracker.service;

import lombok.RequiredArgsConstructor;
import net.szymonsawicki.net.habittracker.tracker.HabitExecutionDTO;
import net.szymonsawicki.net.habittracker.tracker.HabitTrackerExternalApi;
import net.szymonsawicki.net.habittracker.tracker.repository.HabitExecutionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitExecutionService implements HabitTrackerExternalApi {

    private HabitExecutionRepository habitExecutionRepository;

    @Override
    public List<HabitExecutionDTO> findAllExecutionsByHabitId(long habitId) {
        return List.of();
    }

    @Override
    public List<HabitExecutionDTO> findAllExecutionsByUserId(long userId) {
        return List.of();
    }

    @Override
    public HabitExecutionDTO addHabitExectution(HabitExecutionDTO habitExecution) {
        return null;
    }
}
