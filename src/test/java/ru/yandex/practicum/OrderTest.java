package ru.yandex.practicum;

import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.scooter.api.model.Order;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;
import static ru.yandex.practicum.scooter.api.OrderClient.getOrderList;
import static ru.yandex.practicum.scooter.api.OrderClient.makeOrder;

@RunWith(Parameterized.class)


public class OrderTest {

    private final String[] colors;

    public OrderTest(String[] colors) {
        this.colors = colors;
    }

    Response responseMakeOrder;

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}}
        };
    }

    @Before
    public void makeData() {

        Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.",
                4, "+7 800 355 35 35", 5, "2023-06-06", "Saske, come back to Konoha", colors);
        responseMakeOrder = makeOrder(order);
    }

    @Test
    public void makeOrderTest() {

        assertEquals(SC_CREATED, responseMakeOrder.statusCode());
        System.out.println(responseMakeOrder.body().jsonPath().getInt("track"));

//        отменить заказ и почиситить базу невозможно, возвращается ошибка 404
//        OrderTrack orderTrack = new OrderTrack(responseMakeOrder.body().jsonPath().getInt("track"));
//        deleteOrder(orderTrack);
    }
}
