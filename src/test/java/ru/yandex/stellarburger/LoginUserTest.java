package ru.yandex.stellarburger;

import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import io.qameta.allure.*;
import ru.yandex.stellarburger.data.User;
import ru.yandex.stellarburger.request.UserHelper;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Epic("Проверки на логин пользователя в системе")
public class LoginUserTest extends TestBase {

    @Test
    @Story("Успешный логин")
    public void loginCourierTest() {
        UserHelper.createUser(user);
        ValidatableResponse response = UserHelper.login(user);
        accessToken = response.extract().path("accessToken");
        statusCode = response.extract().statusCode();
        boolean isLogin = response.extract().path("success");
        String refreshToken = response.extract().path("refreshToken");
        assertTrue("Пользователь не залогинился", isLogin);
        assertThat("Неверный код статуса", statusCode, is(SC_OK));
        assertThat("Второй токен пустой", refreshToken, notNullValue());
        UserHelper.delete(accessToken.split(" ")[1]);
    }

    @Test
    @Story("Запрос с неверным логином и паролем")
    public void loginCourierWithInvalidPasswordAndNameTest() {
        ValidatableResponse response = UserHelper.login(new User(null, null,
                RandomStringUtils.randomAlphabetic(8)));
        statusCode = response.extract().statusCode();
        boolean isNotLogin = response.extract().path("success");
        message = response.extract().path("message");
        assertFalse("Пользователь залогинился", isNotLogin);
        assertThat("Неверный код статуса", statusCode, is(SC_UNAUTHORIZED));
        assertThat("Неверное сообщение об ошибке", message, equalTo("Email, " +
                "password and name are required fields"));
    }
}

