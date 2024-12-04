package net.szymonsawicki.net.habittracker.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteEvent {
  private Long id;

  public UserDeleteEvent(Long id) {
    this.id = id;
  }
}
