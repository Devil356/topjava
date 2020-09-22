package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = calculateCaloriesPerDay(meals);
        List<UserMealWithExcess> userMealWithExcessesList = fillFilteredMealListWithExcesses();
        for (UserMeal meal : meals) {
            LocalTime userMealTime = meal.getDateTime().toLocalTime();

            if (userMealTime.equals(startTime) || userMealTime.isAfter(startTime) && userMealTime.isBefore(endTime)) {
                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(
                        meal.getDateTime(), meal.getDescription(), meal.getCalories(), false
                );
                userMealWithExcessesList.add(userMealWithExcess);
            }
        }
        return userMealWithExcessesList;
    }

    private static Map<LocalDate, Integer> calculateCaloriesPerDay(List<UserMeal> meals) {
        Map<LocalDate, Integer> caloriesPerDay = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate today = meal.getDateTime().toLocalDate();
            if (caloriesPerDay.isEmpty() || !caloriesPerDay.containsKey(today)) {
                caloriesPerDay.put(today, meal.getCalories());
            } else {
                int calories = caloriesPerDay.get(today);
                caloriesPerDay.put(today, meal.getCalories() + calories);
            }
        }
        return caloriesPerDay;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }
}
