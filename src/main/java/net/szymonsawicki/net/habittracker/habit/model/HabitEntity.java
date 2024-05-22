package net.szymonsawicki.net.habittracker.habit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="habits")
@Getter
@Setter
public class HabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long goalId;
    private Long userId;
    private String name;
    private String description;
}
