package net.szymonsawicki.net.habittracker;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModulithTest {

  ApplicationModules modules = ApplicationModules.of(HabitTrackerApplication.class);

  @Test
  void shouldBeCompliant() {
    modules.verify();
  }

  @Test
  void createModuleDocumentation() {
    ApplicationModules modules = ApplicationModules.of(HabitTrackerApplication.class);
    new Documenter(modules).writeDocumentation().writeIndividualModulesAsPlantUml();
  }
}
