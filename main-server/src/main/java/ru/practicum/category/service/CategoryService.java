package ru.practicum.category.service;

import java.util.List;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.model.Category;

public interface CategoryService {

    Category addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long id);

    Category updateCategory(Long id, NewCategoryDto categoryDto);

    List<Category> findAllCategories(int from, int size);

    Category getCategory(Long id);
}