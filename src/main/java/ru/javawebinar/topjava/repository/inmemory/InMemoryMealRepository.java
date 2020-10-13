package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private static final Logger log = getLogger(InMemoryMealRepository.class);

//    {
//        MealsUtil.meals.forEach(this::save);
//    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage

        try {
            Meal oldMeal = repository.get(meal.getId());
            if (oldMeal.getUserId().equals(userId)) {
                return repository.compute(meal.getId(), (id, oldMeal1)->meal);
            } else {
                log.info("#save: Authentication failed");
                return null;
            }
        } catch (Exception e) {
            log.info("#save: Meal not found");
            return null;
        }
    }

    @Override
    public boolean delete(int mealId, int userId) {
        Meal meal = repository.remove(mealId);
        return meal != null && meal.getUserId().equals(userId);
    }

    @Override
    public Meal get(int mealId, int userId) {
        Meal meal = repository.get(mealId);
        if (meal != null) {
            if (meal.getUserId().equals(userId)) {
                return meal;
            }
            log.info("#get: Authentication failed");
        }
        log.info("#get: Meal not found");
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}

