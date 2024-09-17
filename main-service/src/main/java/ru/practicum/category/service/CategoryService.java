package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(CategoryDto dto);

    void deleteById(long catId);

    CategoryDto update(long catId, CategoryDto dto);

    List<CategoryDto> getAll(Pageable page);

    CategoryDto getCategoryById(long catId);
}
