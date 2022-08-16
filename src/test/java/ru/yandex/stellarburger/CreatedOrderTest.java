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
public class CreatedOrderTest extends TestBase {

    @Test
    @Story("Создание заказа c авторизацией")
    public void createNewOrderWithAuthTest() {
        List<String> ingredients = OrderHelper.getListIngredients();
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = OrderHelper.createOrderWithAuth(new Order(new String[]{ingredients.get(0), ingredients.get(1)}),
                accessToken.split(" ")[1]);
        statusCode = response.extract().statusCode();
        boolean isCreatedOrder = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        assertThat("Неверный код статуса", statusCode, equalTo(SC_OK));
        assertTrue("Заказ не создан", isCreatedOrder);
        assertThat("Номер заказа отсутсвует", orderNumber, notNullValue());
    }

    @Test
    @Story("Создание заказа без авторизации")
    public void createNewOrderWithoutAuthTest() {
        List<String> ingredients = OrderHelper.getListIngredients();
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = OrderHelper.createOrderWithoutAuth(new Order(new String[]{ingredients.get(0), ingredients.get(1)}));
        statusCode = response.extract().statusCode();
        boolean isCreatedOrder = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");
        assertThat("Неверный код статуса", statusCode, equalTo(SC_OK));
        assertTrue("Заказ не создан", isCreatedOrder);
        assertThat("Номер заказа отсутсвует", orderNumber, notNullValue());
    }

    @Test
    @Story("Создание заказа без ингридиентов")
    public void createNewOrderWithoutIngredientsTest() {
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = OrderHelper.createOrderWithAuth(new Order(new String[]{null, null}),
                accessToken.split(" ")[1]);
        statusCode = response.extract().statusCode();
        boolean isNotCreatedOrder = response.extract().path("success");
        message = response.extract().path("message");
        assertThat("Неверный код статуса", statusCode, equalTo(SC_BAD_REQUEST));
        assertFalse("Заказ создан", isNotCreatedOrder);
        assertThat("Неверное сообщение об ошибке", message, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Story("Создание заказа с неверным хешем ингредиентов")
    public void createNewOrderWithInvalidHashTest() {
        response = UserHelper.createUser(user);
        accessToken = response.extract().path("accessToken");
        response = OrderHelper.createOrderWithAuth(new Order(new String[]{"1234", "1234"}),
                accessToken.split(" ")[1]);
        statusCode = response.extract().statusCode();
        assertThat("Неверный код статуса", statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));
    }

}
