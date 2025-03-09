package simulations;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.UUID;

public class ApiGatewaySimulation extends Simulation {

    HttpProtocolBuilder httpProtocol =
            http.baseUrl("http://localhost:8080")
                    .acceptHeader("application/json");

    ScenarioBuilder getUserScenario = scenario("Get User")
            .exec(http("Request 1").get("/api/user/1"));

    ScenarioBuilder addUserScenario = scenario("Add user")
            .exec(session -> session.set("username", UUID.randomUUID().toString().substring(0,20)))
            .exec(http("Request 1").post("/api/user").body(StringBody(
                    """
                    {
                      "username": "{{username}}",
                      "password": "StrngPass123.",
                      "userType": "USER",
                      "userGoals": ["swimming", "running"]
                    }
                    """
            )));

    {
        setUp(
                getUserScenario.injectOpen(constantUsersPerSec(300).during(60)),
                addUserScenario.injectOpen(constantUsersPerSec(300).during(60))
        ).protocols(httpProtocol);
    }
}