package ru.yandex.practicum.scooter.api;

import io.restassured.response.Response;
import ru.yandex.practicum.scooter.api.model.Courier;
import ru.yandex.practicum.scooter.api.model.Order;
import ru.yandex.practicum.scooter.api.model.OrderTrack;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class OrderClient extends BaseApiClient{

    public static Response makeOrder(Order order) {
        return given()
                .spec(getRequestSpec())
                .body(order)
                .when()
                .post(BASE_URL + "/api/v1/orders");
    }

    public static Response getOrderList(){
        return given()
                .spec(getRequestSpec())
                .when()
                .get(BASE_URL + "/api/v1/orders?courierId=");
    }

    public static boolean deleteOrder(OrderTrack orderTrack){
        return given()
                .spec(getRequestSpec())
                .body(orderTrack)
                .when()
                .post(BASE_URL + "/api/v1/orders/cancel")
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .extract()
                .path("ok");

    }
}
