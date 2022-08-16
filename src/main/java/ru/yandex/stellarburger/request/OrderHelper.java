package ru.yandex.stellarburger.request;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.stellarburger.data.Order;

import java.util.List;

import static io.restassured.RestAssured.given;


public class OrderHelper {
    private static final String ORDER_PATH = "https://stellarburgers.nomoreparties.site/api/";

    @Step("Отправка Get запроса на ручку api/ingredients для получения данных об ингредиентах")
    public static List<String> getListIngredients() {
        return given()
                .header("Content-type", "application/json")
                .get(ORDER_PATH + "ingredients")
                .then()
                .extract().path("data._id");
    }


    @Step("Отправка POST запроса c авторизацией на ручку api/order для создание заказа")
    public static ValidatableResponse createOrderWithAuth(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .body(order)
                .post(ORDER_PATH + "orders")
                .then();
    }

    @Step("Отправка POST запроса без авторизации на ручку api/order для создание заказа")
    public static ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post(ORDER_PATH + "orders")
                .then();
    }

    @Step("Отправка Get запроса без авторизации на ручку api/orders для создание заказа")
    public static ValidatableResponse getOrderWithoutAuth() {
        return given()
                .header("Content-type", "application/json")
                .get(ORDER_PATH + "orders")
                .then();
    }

    @Step("Отправка Get запроса с авторизацией на ручку api/orders для создание заказа")
    public static ValidatableResponse getOrderWithAuth(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .get(ORDER_PATH + "orders")
                .then();
    }
}
