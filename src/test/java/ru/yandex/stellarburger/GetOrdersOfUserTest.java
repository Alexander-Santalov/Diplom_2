package ru.yandex.stellarburger;

import io.qameta.allure.*;
import org.junit.Test;
import ru.yandex.stellarburger.data.Order;
import ru.yandex.stellarburger.request.OrderHelper;
import ru.yandex.stellarburger.request.UserHelper;


import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@Epic("Проверки на создание заказа")
public class GetOrdersOfUserTest extends TestBase {

    @Test
    @Story("Получение заказа без авторизации")
    public void getOrderWithoutAuthTest() {
        response = OrderHelper.getOrderWithoutAuth();
        statusCode = response.extract().statusCode();
        boolean isNotGetOrder = response.extract().path("success");
        message = response.extract().path("message");
        assertThat("Неверный код статуса", statusCode, equalTo(SC_UNAUTHORIZED));
        assertFalse("Заказ пользователя получен", isNotGetOrder);
        assertThat("Неверный текст ошибки", message, equalTo("You should be authorised"));
    }

    @Test
    @Story("Получение заказа с авторизацией")
    public void getOrderWithAuthTest() {
        List<String> ingredients = OrderHelper.getListIngredients();
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = OrderHelper.createOrderWithAuth(new Order(new String[]{ingredients.get(0), ingredients.get(1)}),
                accessToken.split(" ")[1]);
        response = OrderHelper.getOrderWithAuth(accessToken.split(" ")[1]);
        statusCode = response.extract().statusCode();
        boolean isGetOrder = response.extract().path("success");
        int total = response.extract().path("total");
        assertThat("Неверный код статуса", statusCode, equalTo(SC_OK));
        assertTrue("Заказ пользователя  не получен", isGetOrder);
        assertThat("Отсутсвует сумма заказа", total, notNullValue());
    }
}
