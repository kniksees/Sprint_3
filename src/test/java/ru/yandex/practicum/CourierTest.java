package ru.yandex.practicum;


import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.Courier;
import ru.yandex.practicum.scooter.api.model.CourierCredentials;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.yandex.practicum.scooter.api.CourierClient.*;


public class CourierTest {


    @Test
    public void courierCreateLoginDeleteTest() {

        Courier courier = Courier.getRandomCourier();
        Response responseCreateCourier = createCourier(courier);

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_OK, responseLoginCourier.statusCode());
        int courierId = responseLoginCourier.body().jsonPath().getInt("id");

        deleteCourier(courierId);
    }

    @Test
    public void canNotCreateTwoIdenticalCourierTest() {
        Courier courier = Courier.getRandomCourier();
        Response responseCreateCourier = createCourier(courier);

        Courier courierClone = courier;
        Response responseCreateCourierClone = createCourier(courierClone);

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        assertEquals(SC_CONFLICT, responseCreateCourierClone.statusCode());

        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_OK, responseLoginCourier.statusCode());
        int courierId = responseLoginCourier.body().jsonPath().getInt("id");

        deleteCourier(courierId);
    }

    @Test
    public void canNotCreateCourierWithOutPassTest() {
        Courier courier = Courier.getRandomCourierWithOutPass();
        Response responseCreateCourier = createCourier(courier);

        assertEquals(SC_BAD_REQUEST, responseCreateCourier.statusCode());
    }

    @Test
    public void canNotLoginWithIncorrectLoginTest() {
        Courier courier = Courier.getRandomCourier();
        Response responseCreateCourier = createCourier(courier);

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        CourierCredentials courierCredentials = new CourierCredentials( courier.getLogin() + "incLogin", courier.getPassword());
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_NOT_FOUND, responseLoginCourier.statusCode());
    }


    //при неверном пароле возвращается ошибка 404 с сообщением "Учетная запись не найдена", я бы возвращал какую-то другую 4хх ошибку с другим сообщением
    @Test
    public void canNotLoginWithIncorrectPasswordTest() {
        Courier courier = Courier.getRandomCourier();
        Response responseCreateCourier = createCourier(courier);

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(),  courier.getPassword() + "incPassword");
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_NOT_FOUND, responseLoginCourier.statusCode());
    }


    //если есть поле логин, но нет пароля, запрос отправляется пока не произойдет 504Gateway time out
    @Test
    public void canNotLoginWithOutLoginTest() {
        Courier courier = Courier.getRandomCourier();
        Response responseCreateCourier = createCourier(courier);

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        CourierCredentials courierCredentials = new CourierCredentials(courier.getPassword());
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_BAD_REQUEST, responseLoginCourier.statusCode());

    }

}
