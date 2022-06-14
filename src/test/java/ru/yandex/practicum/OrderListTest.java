package ru.yandex.practicum;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static ru.yandex.practicum.scooter.api.OrderClient.getOrderList;
import static ru.yandex.practicum.scooter.api.OrderClient.makeOrder;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.scooter.api.model.Order;

public class OrderListTest {

    Response responseOrderList;
    @Before
    public void makeData() {

        responseOrderList = getOrderList();
    }

    @Test
    public void getOrderListTest() {

        assertEquals(SC_OK, responseOrderList.statusCode());
        System.out.println(responseOrderList.body().jsonPath().getString("orders.id"));
    }
}



