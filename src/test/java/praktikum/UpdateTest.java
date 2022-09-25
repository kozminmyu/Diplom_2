package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.client.UserClient;
import praktikum.data.RegisterData;
import praktikum.data.UserData;

// Изменение данных пользователя:
// - С авторизацией,
// - Без авторизации,
// Для обеих ситуаций нужно проверить, что любое поле можно изменить. Для неавторизованного пользователя — ещё и то, что система вернёт ошибку.
@RunWith(Parameterized.class)
public class UpdateTest {

    private UserClient userClient = new UserClient();
    private RegisterData registerData;

    // Токен
    private String accessToken;

    // Параметры для параметризации
    private String email;
    private String name;

    public UpdateTest(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // Параметризация параметров
    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {null, "testname2"},
                {"testemail2@test.com", null},
                {"testemail2@test.com", "testname2"},
        };
    }

    @Before
    public void setUp() {
        userClient.setUp();

        registerData = new RegisterData("testemail1@test.com", "testpassword", "testname1");
        Response response = userClient.register(registerData);

        accessToken = userClient.getAccessToken(response);
    }

    // Изменение данных пользователя с авторизацией
    @Test
    @DisplayName("Update user with authorization")
    public void updateWithAuthorization() {
        UserData userData = new UserData(email, name);
        String expectedEmail;
        String expectedName;

        // Ожидаемые значения email и name
        // Если они не меняются, то должны остаться такими как при регистрации
        if (email == null) {
            expectedEmail = registerData.getEmail();
        } else {
            expectedEmail = email;
        }

        if (name == null) {
            expectedName = registerData.getName();
        } else {
            expectedName = name;
        }

        Response response = userClient.update(accessToken, userData);

        // Проверка всех полей
        userClient.verifyStatusCode(response, 200);
        userClient.verifySuccess(response, true);
        userClient.verifyEmail(response, expectedEmail);
        userClient.verifyName(response, expectedName);

        // Проверка, что данные пользователя действительно изменились
        Response userDataResponse = userClient.getUserData(accessToken);

        userClient.verifyEmail(userDataResponse, expectedEmail);
        userClient.verifyName(userDataResponse, expectedName);
    }

    // Изменение данных пользователя без авторизации
    @Test
    @DisplayName("Update user without authorization")
    public void updateWithoutAuthorization() {
        UserData userData = new UserData(email, name);

        Response response = userClient.update("", userData);

        userClient.verifyStatusCode(response, 401);
        userClient.verifySuccess(response, false);
        userClient.verifyMessage(response, "You should be authorised");
    }

    // Пост-обработка
    @After
    public void tearDown() {
        // Пост-обработка: удаление созданного пользователя
        userClient.delete(accessToken);
    }
}
