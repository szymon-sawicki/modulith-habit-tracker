package net.szymonsawicki.net.habittracker;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class ModulithTest {

  ApplicationModules modules = ApplicationModules.of(HabitTrackerApplication.class);

  @Test
  void shouldBeCompliant() {
    modules.verify();
  }
}
