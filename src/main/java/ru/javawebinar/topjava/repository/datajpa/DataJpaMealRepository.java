package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }


    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(crudUserRepository.getOne(userId));
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = get(id, userId);
        if (meal != null) {
            crudRepository.delete(meal);
            return true;
        }
        throw new NotFoundException("Meal not found!");
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.getMealByIdAndUser_Id(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUser_IdOrderByIdDesc(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {

        return crudRepository.getAllBetweenDates(startDateTime, endDateTime, userId);
    }
}
