package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Assert;
import praktikum.data.OrderData;
import praktikum.data.OrderListResponseData;
import praktikum.data.RegisterData;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static praktikum.constant.EndpointConstant.ORDERS_URI;

public class OrderClient extends RestClient {

    @Step("Send POST request to /api/orders")
    public Response create(String accessToken, OrderData orderData) {
        return given()
                .header("Authorization", accessToken)
                .and()
                .header("Content-type", "application/json")
                .and()
                .body(orderData)
                .when()
                .post(ORDERS_URI);
    }

    @Step("Send GET request to /api/orders")
    public Response getOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .get(ORDERS_URI);
    }

    @Step("Verify result (number)")
    public void verifyNumber(Response response){
        response.then().assertThat().body("order.number", notNullValue());
    }

    @Step("Verify result (name)")
    public void verifyName(Response response){
        response.then().assertThat().body("name", notNullValue());
    }

    @Step("Verify result (total)")
    public void verifyTotal(Response response){
        response.then().assertThat().body("total", notNullValue());
    }

    @Step("Verify result (totalToday)")
    public void verifyTotalToday(Response response){
        response.then().assertThat().body("totalToday", notNullValue());
    }

    public OrderListResponseData getOrderListResponse(Response response) {
        return response.body().as(OrderListResponseData.class);
    }

    @Step("Verify result (_id)")
    public void verifyOrder_id(Response response, int index) {
        OrderListResponseData responseData = getOrderListResponse(response);
        String _id = responseData.getOrders().get(index).get_id();

        Assert.assertNotNull(_id);
    }

    @Step("Verify result (ingredients)")
    public void verifyOrderIngredients(Response response, int index, List<String> expectedIngredients) {
        OrderListResponseData responseData = getOrderListResponse(response);
        List<String> realIngredients = responseData.getOrders().get(index).getIngredients();

        Assert.assertEquals(expectedIngredients, realIngredients);
    }

    @Step("Verify result (status)")
    public void verifyOrderStatus(Response response, int index) {
        OrderListResponseData responseData = getOrderListResponse(response);
        String status = responseData.getOrders().get(index).getStatus();

        Assert.assertEquals("done", status);
    }

    @Step("Verify result (name)")
    public void verifyOrderName(Response response, int index) {
        OrderListResponseData responseData = getOrderListResponse(response);
        String name = responseData.getOrders().get(index).getName();

        Assert.assertNotNull(name);
    }

    @Step("Verify result (createdAt)")
    public void verifyOrderCreatedAt(Response response, int index) {
        OrderListResponseData responseData = getOrderListResponse(response);
        String createdAt = responseData.getOrders().get(index).getCreatedAt();

        Assert.assertNotNull(createdAt);
    }

    @Step("Verify result (updatedAt)")
    public void verifyOrderUpdatedAt(Response response, int index) {
        OrderListResponseData responseData = getOrderListResponse(response);
        String updatedAt = responseData.getOrders().get(index).getUpdatedAt();

        Assert.assertNotNull(updatedAt);
    }

    @Step("Verify result (number)")
    public void verifyOrderNumber(Response response, int index) {
        OrderListResponseData responseData = getOrderListResponse(response);
        int number = responseData.getOrders().get(index).getNumber();

        Assert.assertTrue(number > 0);
    }
}
