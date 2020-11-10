package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.AbstractMealController;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    /*
    Method below is not presented in MealServlet
     */

//    @Override
//    @GetMapping("/meals/{id}")
//    public Meal get(@RequestParam int id) {
//        return super.get(id);
//    }

    @GetMapping("/")
    public String root() {
        return "index";
    }

    @DeleteMapping("/meals/{id}")
    public String deleteMeal(@RequestParam int id) {
        super.delete(id);
        return "meals";
    }

    @GetMapping("/meals")
    public String getMeals(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @PostMapping("/meals")
    public String createMeal(@RequestBody Meal meal) {
        super.create(meal);
        return "meals";
    }

    @PostMapping("/meals/{id}")
    public String updateMeal(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
        return "meals";
    }

    //Not completed yet

    @GetMapping("/meals")
    public String getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, Model model) {
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
