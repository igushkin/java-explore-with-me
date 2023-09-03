package ru.practicum.ewm.admin_access.service.category;

import ru.practicum.ewm.common.dto.category.CategoryDto;
import ru.practicum.ewm.common.dto.category.NewCategoryDto;

public interface AdminCategoriesService {
    CategoryDto create(NewCategoryDto dto);

    void delete(Long catId);

    CategoryDto update(NewCategoryDto dto, Long catId);
}