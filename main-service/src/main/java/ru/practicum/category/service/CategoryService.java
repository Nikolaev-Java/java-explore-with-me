package ru.practicum.category.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto create(NewCategoryDto dto);

    void delete(long catId);

    CategoryDto update(long catId, NewCategoryDto dto);

    List<CategoryDto> getAll(Pageable page);

    CategoryDto getCategory(long catId);
}
