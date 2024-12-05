package net.szymonsawicki.net.habittracker.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeletedEvent {
  private Long id;

  public UserDeletedEvent(Long id) {
    this.id = id;
  }
}
