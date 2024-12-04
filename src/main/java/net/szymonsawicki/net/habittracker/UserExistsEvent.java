package net.szymonsawicki.net.habittracker;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExistsEvent {
  private Long id;

  public UserExistsEvent(Long id) {
    this.id = id;
  }
}
