package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.data.LoginData;
import praktikum.data.RegisterData;
import praktikum.data.UserData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static praktikum.constant.EndpointConstant.*;

public class UserClient extends RestClient {

    @Step("Send POST request to /api/auth/register")
    public Response register(RegisterData registerData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(registerData)
                .when()
                .post(REGISTER_URI);
    }

    @Step("Send POST request to /api/auth/login")
    public Response login(LoginData loginData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginData)
                .when()
                .post(LOGIN_URI);
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_URI);
    }

    @Step("Send PATCH request to /api/auth/user")
    public Response update(String accessToken, UserData userData) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(userData)
                .when()
                .patch(USER_URI);
    }

    @Step("Send GET request to /api/auth/user")
    public Response getUserData(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .get(USER_URI);
    }

    public String getAccessToken(Response response) {
        return response
                .then()
                .extract()
                .body()
                .path("accessToken");
    }

    @Step("Verify result (email)")
    public void verifyEmail(Response response, String email){
        response.then().assertThat().body("user.email", equalTo(email));
    }

    @Step("Verify result (name)")
    public void verifyName(Response response, String name){
        response.then().assertThat().body("user.name", equalTo(name));
    }

    @Step("Verify result (accessToken)")
    public void verifyAccessToken(Response response){
        response.then().assertThat().body("accessToken", containsString("Bearer"));
    }

    @Step("Verify result (refreshToken)")
    public void verifyRefreshToken(Response response){
        response.then().assertThat().body("refreshToken", notNullValue());
    }

}
