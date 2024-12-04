package net.szymonsawicki.net.habittracker.events;

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
