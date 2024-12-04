package net.szymonsawicki.net.habittracker;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitExistsEvent {
  private Long id;

  public HabitExistsEvent(Long id) {
    this.id = id;
  }
}
