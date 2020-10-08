package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface UserDao {

    void createMeal(Meal meal);

    void deleteMeal(long mealId);

    void updateMeal(Meal meal);

    Collection<Meal> getAllMeals();

    Meal getMealById(long mealId);
}
