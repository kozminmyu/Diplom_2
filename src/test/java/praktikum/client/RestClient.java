package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static praktikum.constant.EndpointConstant.BASE_URI;

public class RestClient {
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Step("Verify status code")
    public void verifyStatusCode(Response response, int expectedCode){
        response.then().statusCode(expectedCode);
    }

    @Step("Verify result (success)")
    public void verifySuccess(Response response, boolean isTrue){
        response.then().assertThat().body("success", equalTo(isTrue));
    }

    @Step("Verify result (message)")
    public void verifyMessage(Response response, String message){
        response.then().assertThat().body("message", equalTo(message));
    }
}
