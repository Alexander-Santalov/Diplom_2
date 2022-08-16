package ru.yandex.stellarburger.request;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.stellarburger.data.User;

import static io.restassured.RestAssured.given;

public class UserHelper {
    private static final String COURIER_PATH = "https://stellarburgers.nomoreparties.site/api/auth/";

    @Step("Отправка POST запроса на ручку api/auth/register для создания пользователя")
    public static ValidatableResponse createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(COURIER_PATH + "register")
                .then();
    }

    @Step("Отправка POST запроса на ручку api/auth/login для логина пользователя в систему")
    public static ValidatableResponse login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(COURIER_PATH + "login")
                .then();
    }

    @Step("Отправка DELETE запроса на ручку api/auth/user для удаления пользователя")
    public static void delete(String accessToken) {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .when()
                .delete(COURIER_PATH + "user")
                .then();
    }

    @Step("Отправка PATCH запроса с авторизацией на ручку api/auth/user для обновления данных пользователя")
    public static ValidatableResponse updateUserWithAuth(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(accessToken)
                .body(user)
                .patch(COURIER_PATH + "user")
                .then();
    }

    @Step("Отправка PATCH запроса без авторизации на ручку api/auth/user для обновления данных пользователя")
    public static ValidatableResponse updateUserWithoutAuth(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .patch(COURIER_PATH + "user")
                .then();
    }

}
