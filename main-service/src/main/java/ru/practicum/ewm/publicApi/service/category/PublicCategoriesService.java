package ru.practicum.ewm.publicApi.service.category;

import ru.practicum.ewm.base.dto.Category.CategoryDto;

import java.util.List;

public interface PublicCategoriesService {
    List<CategoryDto> getAll(int from, int size);

    CategoryDto get(Long catId);
}
