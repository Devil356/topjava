package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal getMealByIdAndUser_Id(int id, int userId);

    List<Meal> findAllByUser_IdOrderByIdDesc(int userId);

    @Query(value = "FROM Meal m WHERE m.user.id = :userId " +
            "AND m.dateTime >= :startTime AND m.dateTime < :endTime ORDER BY m.id DESC ")
    List<Meal> getAllBetweenDates(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("userId") int userId
    );

}
