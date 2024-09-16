package ru.practicum.category.dto;

import org.mapstruct.Mapper;
import ru.practicum.category.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toCategoryDto(Category category);

    Category fromNewCategory(NewCategoryDto dto);

    List<CategoryDto> toCategoryDtoList(List<Category> categoryList);
}
