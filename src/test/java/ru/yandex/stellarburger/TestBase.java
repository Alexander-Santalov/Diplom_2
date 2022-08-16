package ru.yandex.stellarburger;

import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import ru.yandex.stellarburger.data.User;
import ru.yandex.stellarburger.request.UserHelper;

public class TestBase {
    protected User user;
    protected String accessToken = "";
    protected ValidatableResponse response;
    protected int statusCode;
    protected String message;


    @Before
    public void setUp() {
        user = new User(RandomStringUtils.randomAlphabetic(10) + "@ya.ru",
                RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
    }

    @After
    public void tearDown() {
        if (accessToken.length() > 5) {
            UserHelper.delete(accessToken.split(" ")[1]);
            accessToken = "";
        }
    }
}
