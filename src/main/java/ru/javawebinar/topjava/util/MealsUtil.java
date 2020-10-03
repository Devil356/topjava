package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class MealsUtil {
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

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDayMap = calculateCaloriesPerDay(meals);
        return fillFilteredMealListWithExcesses(meals, startTime, endTime, caloriesPerDay, caloriesPerDayMap);
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

    private static List<UserMealWithExcess> fillFilteredMealListWithExcesses(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay, Map<LocalDate, Integer> caloriesPerDayMap) {
        List<UserMealWithExcess> userMealWithExcessesList = new ArrayList<>();
        for (UserMeal meal : meals) {
            LocalTime userMealTime = meal.getDateTime().toLocalTime();
            if (userMealTime.equals(startTime) || userMealTime.isAfter(startTime) && userMealTime.isBefore(endTime)) {
                boolean isExcess = caloriesPerDayMap.get(meal.getDateTime().toLocalDate()).compareTo(caloriesPerDay) > 0;
                userMealWithExcessesList.add(new UserMealWithExcess(
                        meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess
                ));
            }
        }
        return userMealWithExcessesList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> collect = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(),
                        Collectors.mapping(UserMeal::getCalories,
                                Collectors.reducing(0, Integer::sum))));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(
                        userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        collect.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)
                )
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final Map<LocalDate, Integer> sumCaloriesPerDay = new HashMap<>();
        final Map<LocalDate, AtomicBoolean> excededPerDay = new HashMap<>();

        final List<UserMealWithExcess> mealWithExcesses = new ArrayList<>();

        meals.forEach(userMeal -> {
            AtomicBoolean wrapBoolean = excededPerDay.computeIfAbsent(userMeal.getDateTime().toLocalDate(), localDate -> new AtomicBoolean());
            Integer dailyCalories = sumCaloriesPerDay.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
            if (dailyCalories > caloriesPerDay) {
                wrapBoolean.set(true);
            }
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealWithExcesses.add(createWithExceeded(userMeal, wrapBoolean));
            }
        });
        return mealWithExcesses;
    }

    private static UserMealWithExcess createWithExceeded(UserMeal userMeal, AtomicBoolean wrapBoolean) {
        return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), wrapBoolean.get());
    }

}
