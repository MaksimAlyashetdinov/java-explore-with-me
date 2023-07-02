package ru.practicum.category.controller;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class UsersCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> findAllCategories(
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Get all categories request.");
        return categoryService.findAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public Category findCategoryById(@PathVariable @NotNull Long catId) {
        log.info("Get category with id {} request.", catId);
        return categoryService.getCategory(catId);
    }
}