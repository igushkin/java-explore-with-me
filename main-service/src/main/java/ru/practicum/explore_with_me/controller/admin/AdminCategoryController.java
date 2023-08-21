package ru.practicum.explore_with_me.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.dto.NewCategoryDto;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.service.CategoryService;
import ru.practicum.explore_with_me.service.UserEventService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final UserEventService userEventService;

    @PostMapping()
    public CategoryDto create(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("{}: Запрос к эндпоинту '{}' на добавление категории {}", newCategoryDto.toString());
        return categoryService.create(categoryMapper.newCategoryDtoToCategoryDto(newCategoryDto));
    }

    @PatchMapping()
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("{}: Запрос к эндпоинту '{}' на изменение категории {}", categoryDto.toString());
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void delete(@PathVariable Long categoryId) {
        log.info("{}: Запрос к эндпоинту '{}' на удаление категории с ID={}", categoryId);
        userEventService.deleteCategory(categoryId);
    }
}