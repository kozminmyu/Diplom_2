package praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.OrderClient;
import praktikum.client.UserClient;
import praktikum.data.OrderData;
import praktikum.data.RegisterData;

import java.util.Arrays;
import java.util.List;

// Получение заказов конкретного пользователя:
// - Авторизованный пользователь,
// - Неавторизованный пользователь.
public class OrderListTest {
    private UserClient userClient = new UserClient();
    private OrderClient orderClient = new OrderClient();

    // Токен
    private String accessToken;

    @Before
    public void setUp() {
        userClient.setUp();

        RegisterData registerData = new RegisterData("testemail1@test.com", "testpassword", "testname1");
        Response response = userClient.register(registerData);

        accessToken = userClient.getAccessToken(response);
    }

    // Получение списка заказов с аворизацией
    @Test
    @DisplayName("Get order list with authorization")
    public void getOrderListWithAuthorization() {
        List<String> firstIngredients = Arrays.asList("61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa6c");
        List<String> secondIngredients = Arrays.asList("61c0c5a71d1f82001bdaaa76", "61c0c5a71d1f82001bdaaa75", "61c0c5a71d1f82001bdaaa6d");
        OrderData firstOrderData = new OrderData(firstIngredients);
        OrderData secondOrderData = new OrderData(secondIngredients);

        // Создание двух заказов (для наглядности)
        orderClient.create(accessToken, firstOrderData);
        orderClient.create(accessToken, secondOrderData);

        Response response = orderClient.getOrders(accessToken);

        // Проверка обших параметров ответа
        orderClient.verifyStatusCode(response, 200);
        orderClient.verifySuccess(response,true);
        orderClient.verifyTotal(response);
        orderClient.verifyTotalToday(response);

        // Проверка первого заказа
        orderClient.verifyOrder_id(response, 0);
        orderClient.verifyOrderIngredients(response, 0, firstIngredients);
        orderClient.verifyOrderStatus(response, 0);
        orderClient.verifyOrderName(response, 0);
        orderClient.verifyOrderCreatedAt(response, 0);
        orderClient.verifyOrderUpdatedAt(response, 0);
        orderClient.verifyOrderNumber(response, 0);

        // Проверка второго заказа
        orderClient.verifyOrder_id(response, 1);
        orderClient.verifyOrderIngredients(response, 1, secondIngredients);
        orderClient.verifyOrderStatus(response, 1);
        orderClient.verifyOrderName(response, 1);
        orderClient.verifyOrderCreatedAt(response, 1);
        orderClient.verifyOrderUpdatedAt(response, 1);
        orderClient.verifyOrderNumber(response, 1);
    }

    // Получение списка заказов без авторизации
    @Test
    @DisplayName("Get order list without authorization")
    public void getOrderListWithoutAuthorization() {

        // Заказы не создаю, так как они не играют роли в ответе
        Response response = orderClient.getOrders("");

        orderClient.verifyStatusCode(response, 401);
        orderClient.verifySuccess(response, false);
        orderClient.verifyMessage(response, "You should be authorised");
    }

    // Пост-обработка
    @After
    public void tearDown() {
        // Пост-обработка: удаление созданного пользователя
        userClient.delete(accessToken);
    }
}
