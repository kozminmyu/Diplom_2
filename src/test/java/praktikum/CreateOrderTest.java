package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.data.OrderData;
import praktikum.data.RegisterData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Создание заказа:
// - C авторизацией,
// - Без авторизации,
// - С ингредиентами,
// - Без ингредиентов,
// - С неверным хешем ингредиентов.
@RunWith(Parameterized.class)
public class CreateOrderTest {
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();

    // Токен
    private String accessToken;
    private String realToken;

    // Параметр для параметризации
    // Результаты с авторизацией и без авторизации совпадают, поэтому необходимость использования токена вынесена в отдельный параметр
    private boolean shouldAuthorize;

    public CreateOrderTest(boolean shouldAuthorize) {
        this.shouldAuthorize = shouldAuthorize;
    }

    // Параметризация параметра
    @Parameterized.Parameters(name = "Use authorization token: {0}")
    public static Object[] getData() {
        return new Object[]{
                true,
                false
        };
    }

    @Before
    public void setUp() {
        userClient.setUp();

        RegisterData registerData = new RegisterData("testemail1@test.com", "testpassword", "testname1");
        Response response = userClient.register(registerData);

        accessToken = userClient.getAccessToken(response);

        // Реально используемый токен
        if (shouldAuthorize) {
            realToken = accessToken;
        } else {
            realToken = "";
        }

    }

    // Создание заказа с ингридиентами
    @Test
    @DisplayName("Create order with correct ingredients")
    public void createOrderWithIngredients() {
        List<String> ingredients = Arrays.asList("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa6c");
        OrderData orderData = new OrderData(ingredients);

        Response response = orderClient.create(realToken, orderData);

        // Проверка всех полей
        // Для тех полей, про логику заполнения которых ничего неизвестно, проверяем на существование
        orderClient.verifyStatusCode(response, 200);
        orderClient.verifySuccess(response, true);
        orderClient.verifyName(response);
        orderClient.verifyNumber(response);
    }

    // Создание заказа без ингредиентов
    @Test
    @DisplayName("Create order without ingredients")
    public void createOrderWithoutIngredients() {
        List<String> ingredients = new ArrayList<>();
        OrderData orderData = new OrderData(ingredients);

        Response response = orderClient.create(realToken, orderData);

        orderClient.verifyStatusCode(response, 400);
        orderClient.verifySuccess(response, false);
        orderClient.verifyMessage(response,"Ingredient ids must be provided");
    }

    // Создание заказа с ингридиентами
    @Test
    @DisplayName("Create order with incorrect ingredients")
    public void createOrderWithIncorrectIngredients() {
        List<String> ingredients = Arrays.asList("wrong ingredient", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa6c");
        OrderData orderData = new OrderData(ingredients);

        Response response = orderClient.create(realToken, orderData);

        // Согласно спецификации, возвращается код 500 Internal Server Error
        orderClient.verifyStatusCode(response, 500);
    }

    // Пост-обработка
    @After
    public void tearDown() {
        // Пост-обработка: удаление созданного пользователя
        userClient.delete(accessToken);
    }
}
