package net.szymonsawicki.net.habittracker.gateway;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import net.szymonsawicki.net.habittracker.goalmanagement.GoalExternalAPI;
import net.szymonsawicki.net.habittracker.usermanagement.UserExternalAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayIntegrationTests {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private UserExternalAPI userExternalAPI;
  @Autowired private GoalExternalAPI goalExternalAPI;

  @Test
  void shouldReturnUserWithGoalsAndHabits() {

    // given
    var response = testRestTemplate.postForEntity("/test-data/", null, String.class);

    // when
    var firstUserId = userExternalAPI.findAllUsers().getFirst().id();
    var userWithGoals = goalExternalAPI.findUserWithGoals(firstUserId);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();

    assertThat(userWithGoals).isNotNull();
    assertThat(userWithGoals.userGoals()).isNotNull();
    assertThat(userWithGoals.userGoals().size()).isEqualTo(1);
    assertThat(userWithGoals.userGoals().getFirst().habits().size()).isEqualTo(1);
  }
}
