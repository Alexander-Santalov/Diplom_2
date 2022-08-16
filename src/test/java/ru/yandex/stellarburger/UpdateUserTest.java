package ru.yandex.stellarburger;


import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import ru.yandex.stellarburger.data.User;
import ru.yandex.stellarburger.request.UserHelper;

import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Epic("Проверки на обновление пользователя")
public class UpdateUserTest extends TestBase {

    @Test
    @Story("Обновление пользователя с авторизацией")
    public void updateUserWithAuthTest() {
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = UserHelper.updateUserWithAuth(new User(RandomStringUtils.randomAlphabetic(10) + "@ya.ru",
                "55555555", "Santalov12"), accessToken.split(" ")[1]);
        statusCode = response.extract().statusCode();
        boolean isUpdate = response.extract().path("success");
        assertThat("Неверный код статуса", statusCode, equalTo(SC_OK));
        assertTrue("Пользователь не обновился", isUpdate);
    }

    @Test
    @Story("Обновление пользователя без авторизации")
    public void updateUserWithoutAuthTest() {
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = UserHelper.updateUserWithoutAuth(new User(
                RandomStringUtils.randomAlphabetic(10) + "@ya.ru", "55555555", "Santalov12"));
        statusCode = response.extract().statusCode();
        boolean isNotUpdate = response.extract().path("success");
        message = response.extract().path("message");
        assertThat("Неверный код статуса", statusCode, equalTo(SC_UNAUTHORIZED));
        assertFalse("Пользователь обновился", isNotUpdate);
        assertThat("Неверное сообщение об ошибке", message, equalTo("You should be authorised"));
    }

    @Test
    @Story("Обновление пользователя с авторизацией и повторяемой почтой")
    public void updateUserWithAuthRepeatEmailTest() {
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        String email = user.getEmail();
        response = UserHelper.createUser(new User(RandomStringUtils.randomAlphabetic(10) + "@ya.ru",
                RandomStringUtils.randomAlphabetic(8), RandomStringUtils.randomAlphabetic(8)));
        String accessToken_new = response.extract().path("accessToken");
        response = UserHelper.updateUserWithAuth(new User(email, RandomStringUtils.randomAlphabetic(8),
                RandomStringUtils.randomAlphabetic(8)), accessToken_new.split(" ")[1]);
        statusCode = response.extract().statusCode();
        boolean isUpdate = response.extract().path("success");
        message = response.extract().path("message");
        assertThat("Неверный код статуса", statusCode, equalTo(SC_FORBIDDEN));
        assertFalse("Пользователь обновился", isUpdate);
        assertThat("Неверное сообщение об ошибке", message, equalTo("User with such email already exists"));
        UserHelper.delete(accessToken_new.split(" ")[1]);
    }
}