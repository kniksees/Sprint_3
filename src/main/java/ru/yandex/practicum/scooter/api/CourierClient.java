package ru.yandex.practicum.scooter.api;

import io.restassured.response.Response;
import ru.yandex.practicum.scooter.api.model.Courier;
import ru.yandex.practicum.scooter.api.model.CourierCredentials;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class CourierClient extends BaseApiClient{

    public static Response createCourier(Courier courier) {
        return given()
                .spec(getRequestSpec())
                .body(courier)
                .when()
                .post(BASE_URL + "/api/v1/courier");
    }

    public static Response loginCourier(CourierCredentials courierCredentials) {
         return given()
                .spec(getRequestSpec())
                .body(courierCredentials)
                .when()
                .post(BASE_URL + "/api/v1/courier/login");
    }

    public static boolean deleteCourier(int courier){
        return given()
                .spec(getRequestSpec())
                .when()
                .delete(BASE_URL + "/api/v1/courier/" + courier)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("ok");

    }
}
