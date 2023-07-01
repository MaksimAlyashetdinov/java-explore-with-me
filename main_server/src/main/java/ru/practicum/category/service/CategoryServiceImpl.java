package ru.practicum.category.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(NewCategoryDto newCategoryDto) {
        validateCategory(newCategoryDto);
        log.info("Create category with name {}", newCategoryDto.getName());
        return categoryRepository.save(CategoryMapper.mapToCategoryFromNew(newCategoryDto));
    }

    @Override
    public void deleteCategory(Integer id) {
        containsCategory(id);
        log.info("Delete category with id {}", id);
        categoryRepository.deleteById(id);
    }

    @Override
    public Category updateCategory(Integer id, NewCategoryDto newCategoryDto) {
        containsCategory(id);
        Category categoryByName = categoryRepository.findByName(newCategoryDto.getName());
        if (categoryByName != null && categoryByName.getId().equals(id)) {
            throw new ConflictException("Category already exist.");
        }
        log.info("Update category with id {} to {}", id, newCategoryDto.getName());
        return categoryRepository.saveAndFlush(Category.builder()
                                                       .id(id)
                                                       .name(newCategoryDto.getName())
                                                       .build());
    }

    @Override
    public List<Category> findAllCategories(int from, int size) {
        log.info("Get all categories.");
        Pageable pageable = PageRequest.of(from, size);
        return categoryRepository.findAll(pageable)
                                 .stream()
                                 .collect(Collectors.toList());
    }

    @Override
    public Category getCategory(Integer id) {
        containsCategory(id);
        log.info("Get category with id {}.", id);
        return categoryRepository.findById(id).get();
    }

    private void validateCategory(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName().isBlank()) {
            throw new ValidationException("The category name cannot be empty.");
        }
        if (categoryRepository.findByName(newCategoryDto.getName()) != null) {
            throw new ConflictException("A category with this name already exists.");
        }
    }

    private void containsCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new NotFoundException("The category with this id was not found.");
        }
    }
}