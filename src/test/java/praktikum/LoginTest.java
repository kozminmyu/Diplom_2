package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.data.LoginData;
import praktikum.data.RegisterData;

// Логин пользователя:
// - Логин под существующим пользователем,
// - Логин с неверным логином и паролем.
public class LoginTest {
    private UserClient userClient = new UserClient();

    // Токен
    private String accessToken;

    // Признак необходмости удаления пользователя
    private boolean shouldDeleteUser;

    @Before
    public void setUp() {
        userClient.setUp();
    }

    // Авторизация для существующего пользователя
    @Test
    @DisplayName("Login as an existing user")
    public void LoginExistingUser() {
        shouldDeleteUser = true;

        RegisterData registerData = new RegisterData("testemail1@test.com", "testpassword", "testname1");
        LoginData loginData = new LoginData(registerData);

        userClient.register(registerData);
        Response response = userClient.login(loginData);

        accessToken = userClient.getAccessToken(response);

        // Проверка всех полей
        // Для токеном проверяем существование
        userClient.verifyStatusCode(response, 200);
        userClient.verifySuccess(response, true);
        userClient.verifyEmail(response, registerData.getEmail());
        userClient.verifyName(response, registerData.getName());
        userClient.verifyAccessToken(response);
        userClient.verifyRefreshToken(response);
    }

    // Авторизация по некорректному паролю
    @Test
    @DisplayName("Login with an incorrect password")
    public void LoginWrongPassword() {
        shouldDeleteUser = true;

        RegisterData registerData = new RegisterData("testemail1@test.com", "testpassword", "testname1");
        LoginData loginData = new LoginData(registerData.getEmail(), "wrongpassword");

        Response registerResponse = userClient.register(registerData);
        Response response = userClient.login(loginData);

        accessToken = userClient.getAccessToken(registerResponse);

        userClient.verifyStatusCode(response, 401);
        userClient.verifyMessage(response, "email or password are incorrect");
    }

    // Авторизация для несуществующего пользователя
    @Test
    @DisplayName("Login as a nonexistent user")
    public void LoginNonexistentUser() {
        shouldDeleteUser = false;

        LoginData loginData = new LoginData("testemail1@test.com", "testpassword");

        Response response = userClient.login(loginData);

        userClient.verifyStatusCode(response, 401);
        userClient.verifyMessage(response, "email or password are incorrect");
    }

    // Пост-обработка
    @After
    public void tearDown() {
        if(shouldDeleteUser) {
            // Пост-обработка: удаление созданного пользователя
            userClient.delete(accessToken);
        }
    }
}
