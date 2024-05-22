package net.szymonsawicki.net.habittracker.goal;

import net.szymonsawicki.net.habittracker.habit.HabitDTO;

import java.util.ArrayList;
import java.util.List;

public record GoalDTO(Long id, Long userId, String name, String description, List<HabitDTO> habits) {
    public GoalDTO(Long id, Long userId, String name, String description) {
        this(id,userId,name,description,new ArrayList<>());
    }
}
