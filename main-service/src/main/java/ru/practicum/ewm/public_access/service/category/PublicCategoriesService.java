package ru.practicum.ewm.public_access.service.category;

import ru.practicum.ewm.common.dto.category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(int from, int size);

    CategoryDto get(Long catId);
}