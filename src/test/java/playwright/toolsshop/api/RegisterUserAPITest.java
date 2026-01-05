package playwright.toolsshop.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import playwright.toolsshop.domain.User;

@UsePlaywright
public class RegisterUserAPITest {

    private APIRequestContext request;
    private Gson gson = new Gson();


    @BeforeEach
    void setup(Playwright playwright) {
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
    }

    @AfterEach
    void teardown() {
        if (request != null) {
            request.dispose();
        }
    }

    @Test
    void shouldCreateUser() {
        User validUser = User.randomUser();
        System.out.println("Request Body: " + new Gson().toJson(validUser));

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );


        String responseBody = response.text();
        System.out.println("Response Body: " + responseBody);

        User createdUser = gson.fromJson(responseBody, User.class);

        Assertions.assertThat(createdUser).isEqualTo(validUser.withPassword(null));
        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should return status code 201")
                    .isEqualTo(201);

            softly.assertThat(createdUser)
                    .as("Registration should return the created user")
                    .isEqualTo(validUser.withPassword(null));

            softly.assertThat(responseObject.has("password"))
                    .as("No password should be returned in the response")
                    .isFalse();

            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an id")
                    .isNotEmpty();

            softly.assertThat(response.headers().get("content-type"))
                    .contains("application/json");

        });


    }

    @Test
    void firstNameShouldBeRequired(){

        User withNoName = new User(null,
                "Ernser",
                "agueda.mann@hotmail.com",
                User.randomAddress(),
                "SuperSecure@123",
                "(305) 294-2996",
                "1984-01-01");

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(withNoName)
        );

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("")
                    .isEqualTo(422);

            JsonObject responseObject = gson.fromJson(response.text(), JsonObject.class);
            softly.assertThat(responseObject.has("first_name")).isTrue();

            String errorMessage = responseObject.get("first_name").getAsString();
            softly.assertThat(errorMessage).contains("The first name field is required.");
        });
    }
}

