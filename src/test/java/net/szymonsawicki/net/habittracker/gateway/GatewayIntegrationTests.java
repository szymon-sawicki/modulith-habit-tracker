package net.szymonsawicki.net.habittracker.gateway;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import net.szymonsawicki.net.habittracker.user.UserExternalAPI;
import net.szymonsawicki.net.habittracker.user.UserInternalAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GatewayIntegrationTests {

  @Autowired private TestRestTemplate testRestTemplate;

  @Autowired private UserExternalAPI userExternalAPI;
  @Autowired private UserInternalAPI userInternalAPI;

  @BeforeEach
  public void loadDataIntoDb() {

    // when
    var response = testRestTemplate.postForEntity("/test-data/", null, String.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
  }

  @Test
  void shouldReturnUserWithGoalsAndHabits() {

    var firstUserId = userExternalAPI.findAllUsers().getFirst().id();
    var userWithGoals = userExternalAPI.findByIdWithGoalsAndHabits(firstUserId);

    assertThat(userWithGoals).isNotNull();
    assertThat(userWithGoals.goals()).isNotNull();
    assertThat(userWithGoals.goals().size()).isEqualTo(1);
    assertThat(userWithGoals.goals().getFirst().habits().size()).isEqualTo(1);
  }
}
