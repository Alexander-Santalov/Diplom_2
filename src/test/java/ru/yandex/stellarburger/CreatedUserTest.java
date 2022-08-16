package ru.yandex.stellarburger;

import io.qameta.allure.*;
import org.junit.Test;
import ru.yandex.stellarburger.request.UserHelper;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

@Epic("Проверки на создание пользователя")
public class CreatedUserTest extends TestBase {


    @Test
    @Story("Успешное создание учетной записи")
    public void createNewUserTest() {
        response = UserHelper.createUser(user);
        statusCode = response.extract().statusCode();
        boolean isCreatedUser = response.extract().path("success");
        assertThat("Неверный код статуса", statusCode, is(SC_OK));
        assertTrue("Пользователь не создан", isCreatedUser);
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @Story("Создание учетной записи с неуникальной почтой")
    public void createRepeatUserTest() {
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = UserHelper.createUser(user);
        statusCode = response.extract().statusCode();
        message = response.extract().path("message");
        assertThat("Неверный код статуса", statusCode, is(SC_FORBIDDEN));
        assertThat("Неверное сообщение об ошибке", message, equalTo("User already exists"));
    }

    @Test
    @Story("Создание учетной записи без почты")
    public void createNewUserWithoutEmailTest() {
        user.setEmail(null);
        response = UserHelper.createUser(user);
        statusCode = response.extract().statusCode();
        message = response.extract().path("message");
        assertThat("Неверный код статуса", statusCode, is(SC_FORBIDDEN));
        assertThat("Неверное сообщение об ошибке", message,
                equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Создание учетной записи без пароля")
    public void createNewUserWithoutPasswordTest() {
        user.setPassword(null);
        response = UserHelper.createUser(user);
        statusCode = response.extract().statusCode();
        message = response.extract().path("message");
        assertThat("Неверный код статуса", statusCode, is(SC_FORBIDDEN));
        assertThat("Неверное сообщение об ошибке", message,
                equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Создание учетной записи без имени")
    public void createNewUserWithoutNameTest() {
        user.setName(null);
        response = UserHelper.createUser(user);
        statusCode = response.extract().statusCode();
        message = response.extract().path("message");
        assertThat("Неверный код статуса", statusCode, is(SC_FORBIDDEN));
        assertThat("Неверное сообщение об ошибке", message,
                equalTo("Email, password and name are required fields"));
    }


}


