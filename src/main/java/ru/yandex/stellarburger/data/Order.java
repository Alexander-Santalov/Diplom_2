package ru.yandex.stellarburger.data;

public class Order {
    public String[] ingredients;

    public Order(String[] ingredients) {
        this.ingredients = ingredients;
    }
    public Order() {
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

}

