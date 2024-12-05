package net.szymonsawicki.net.habittracker.events;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserCreatedEvent {
  private Long id;
  private List<String> goalNames;
}
