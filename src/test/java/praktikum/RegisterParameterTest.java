package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.client.UserClient;
import praktikum.data.RegisterData;

// Вывел тесты по проверке ошибки регистрации из-за отсутствия обязательного параметра отдельно,
// чтобы параметризация не задела те методы, где она не нужна
@RunWith(Parameterized.class)
public class RegisterParameterTest {

    private UserClient userClient = new UserClient();

    // Параметры для параметризации
    private String email;
    private String password;
    private String name;

    public RegisterParameterTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // Параметризация параметров
    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {null, "testpassword", "testname1"},
                {"", "testpassword", "testname1"},
                {"testemail1@test.com", null, "testname1"},
                {"testemail1@test.com", "", "testname1"},
                {"testemail1@test.com", "testpassword", null},
                {"testemail1@test.com", "testpassword", ""}
        };
    }

    @Before
    public void setUp() {
        userClient.setUp();
    }

    // Регистрация без указания обязательного параметра
    @Test
    @DisplayName("Register without a mandatory parameter")
    public void registerNoMandatoryParameter() {

        RegisterData registerData = new RegisterData(email, password, name);

        Response response = userClient.register(registerData);

        userClient.verifyStatusCode(response, 403);
        userClient.verifySuccess(response, false);
        userClient.verifyMessage(response, "Email, password and name are required fields");
    }

}
