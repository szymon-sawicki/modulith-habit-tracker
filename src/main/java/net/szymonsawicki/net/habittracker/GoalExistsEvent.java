package net.szymonsawicki.net.habittracker;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoalExistsEvent {
  private Long id;

  public GoalExistsEvent(Long id) {
    this.id = id;
  }
}
