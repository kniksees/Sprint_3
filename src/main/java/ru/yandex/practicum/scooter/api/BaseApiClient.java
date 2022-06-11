package ru.yandex.practicum.scooter.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseApiClient {
    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    public static RequestSpecification getRequestSpec(){
        return new RequestSpecBuilder().log(LogDetail.ALL)
                .setContentType(ContentType.JSON).build();
    }
}
