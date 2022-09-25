package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.data.RegisterData;

// Создание пользователя:
// - Создать уникального пользователя;
// - Создать пользователя, который уже зарегистрирован;
// Не требующий парамеризации тесты регистрации
// Кейс "Создать пользователя и не заполнить одно из обязательных полей" заведен в отдельный класс с параметризацией
public class RegisterTest {

    private UserClient userClient = new UserClient();

    // Токен
    private String accessToken;

    @Before
    public void setUp() {
        userClient.setUp();
    }

    // Регистрация уникального пользователя
    @Test
    @DisplayName("Register a unique user")
    public void registerUniqueUser() {
        RegisterData registerData = new RegisterData("testemail1@test.com", "testpassword", "testname1");

        Response response = userClient.register(registerData);

        accessToken = userClient.getAccessToken(response);

        // Проверка всех полей
        // Для тех полей, про логику заполнения которых ничего неизвестно, проверяем на существование
        userClient.verifyStatusCode(response, 200);
        userClient.verifySuccess(response, true);
        userClient.verifyEmail(response, registerData.getEmail());
        userClient.verifyName(response, registerData.getName());
        userClient.verifyAccessToken(response);
        userClient.verifyRefreshToken(response);
    }

    // Регистрация уже зарегистрированного пользователя
    @Test
    @DisplayName("Register an already registered user")
    public void registerRegisteredUser() {
        RegisterData registerData = new RegisterData("testemail1@test.com", "testpassword", "testname1");

        Response firstResponse = userClient.register(registerData);
        Response secondResponse = userClient.register(registerData);

        accessToken = userClient.getAccessToken(firstResponse);

        userClient.verifyStatusCode(secondResponse, 403);
        userClient.verifySuccess(secondResponse, false);
        userClient.verifyMessage(secondResponse, "User already exists");
    }

    // Пост-обработка
    @After
    public void tearDown() {
        // Пост-обработка: удаление созданного пользователя
        userClient.delete(accessToken);
    }
}
