package ru.yandex.practicum;


import io.restassured.response.Response;
import org.junit.After;
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

    int courierId = -1;
    Courier courier;
    Response responseCreateCourier;

    @Before
    public void makeData() {
        courier = Courier.getRandomCourier();
        responseCreateCourier = createCourier(courier);
    }

    @After
    public void deleteCourierAfter(){
        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_OK, responseLoginCourier.statusCode());

        courierId = responseLoginCourier.body().jsonPath().getInt("id");

        if (courierId != -1) {
            deleteCourier(courierId);
        }
        courierId = -1;
    }

    @Test
    public void courierCreateLoginDeleteTest() {

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));
    }

    @Test
    public void canNotCreateTwoIdenticalCourierTest() {

        Courier courierClone = courier;
        Response responseCreateCourierClone = createCourier(courierClone);

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        assertEquals(SC_CONFLICT, responseCreateCourierClone.statusCode());
    }

    @Test
    public void canNotCreateCourierWithOutPassTest() {
        Courier courierWithOutPass = Courier.getRandomCourierWithOutPass();
        Response responseCreateCourier = createCourier(courierWithOutPass);

        assertEquals(SC_BAD_REQUEST, responseCreateCourier.statusCode());
    }

    @Test
    public void canNotLoginWithIncorrectLoginTest() {

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        CourierCredentials courierCredentials = new CourierCredentials( courier.getLogin() + "incLogin", courier.getPassword());
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_NOT_FOUND, responseLoginCourier.statusCode());
}


    //при неверном пароле возвращается ошибка 404 с сообщением "Учетная запись не найдена", я бы возвращал какую-то другую 4хх ошибку с другим сообщением
    @Test
    public void canNotLoginWithIncorrectPasswordTest() {

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        CourierCredentials courierCredentials = new CourierCredentials(courier.getLogin(),  courier.getPassword() + "incPassword");
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_NOT_FOUND, responseLoginCourier.statusCode());
}

    @Test
    public void canNotLoginWithOutLoginTest() {

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        CourierCredentials courierCredentials = new CourierCredentials();
        courierCredentials.setPassword(courier.getPassword());
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_BAD_REQUEST, responseLoginCourier.statusCode());
    }

    @Test
    public void canNotLoginWithOutPasswordTest() {

        assertEquals(SC_CREATED, responseCreateCourier.statusCode());
        assertTrue(responseCreateCourier.body().jsonPath().getBoolean("ok"));

        CourierCredentials courierCredentials = new CourierCredentials();
        courierCredentials.setLogin(courier.getLogin());
        Response responseLoginCourier = loginCourier(courierCredentials);
        assertEquals(SC_BAD_REQUEST, responseLoginCourier.statusCode());
    }
}
