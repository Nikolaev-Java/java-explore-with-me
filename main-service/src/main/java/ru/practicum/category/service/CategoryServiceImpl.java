package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;
    private static final String ERROR_NOT_FOUND_MSG = "Category not found";

    @Override
    public CategoryDto create(CategoryDto dto) {
        log.debug("Received request new category: {}", dto);
        Category saved = repository.save(mapper.fromNewCategory(dto));
        log.debug("Create new category: {}", dto);
        return mapper.toCategoryDto(saved);
    }

    @Override
    public void deleteById(long catId) {
        log.debug("Received request delete category. Id: {}", catId);
        if (!repository.existsById(catId)) {
            throw new NotFoundException(ERROR_NOT_FOUND_MSG);
        }
        repository.deleteById(catId);
        log.debug("Delete category: {}", catId);
    }

    @Override
    @Transactional
    public CategoryDto update(long catId, CategoryDto dto) {
        log.debug("Received request update category. Id: {}. Dto - {}", catId, dto);
        Category categoryToUpdate = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_MSG));
        categoryToUpdate.setName(dto.getName());
        repository.save(categoryToUpdate);
        log.debug("Update category: {}", dto);
        return mapper.toCategoryDto(categoryToUpdate);
    }

    @Override
    public List<CategoryDto> getAll(Pageable page) {
        log.debug("Received request getAll category page.");
        List<Category> categories = repository.findAll(page).getContent();
        log.debug("Get all category size: {}", categories.size());
        return mapper.toCategoryDtoList(categories);
    }

    @Override
    public CategoryDto getCategoryById(long catId) {
        log.debug("Received request getCategory category. Id: {}", catId);
        Category categoryToUpdate = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_MSG));
        log.debug("Get category: {}", categoryToUpdate);
        return mapper.toCategoryDto(categoryToUpdate);
    }
}
