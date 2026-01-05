package playwright.toolsshop.login;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import playwright.toolsshop.PlaywrightRunConfiguration;
import playwright.toolsshop.domain.User;

public class LoginWithRegisteredUserTest extends PlaywrightRunConfiguration {

    @Test
    @DisplayName("Should be able to login with registered user")
    void login_with_registered_user() {
//        Register a user via api
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);
//        Login
        LoginPage loginPAge = new LoginPage(page);
        loginPAge.open();
        loginPAge.loginAs(user);

//        Check we are on the wright account page
        Assertions.assertThat(loginPAge.title()).isEqualTo("My account");
    }

    @Test
    @DisplayName("Reject user creation if pasword is not valid")
    void reject_user_creation_if_password_is_not_valid(){
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);
//        Login
        LoginPage loginPAge = new LoginPage(page);
        loginPAge.open();
        loginPAge.loginAs(user.withPassword("invalid"));

        Assertions.assertThat(loginPAge.loginErrorMessage()).isEqualTo("Invalid email or password");
    }
}
