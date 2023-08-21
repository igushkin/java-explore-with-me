package ru.practicum.explore_with_me.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping()
    public Collection<CategoryDto> getCategories(@Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("{}: Запрос к эндпоинту '{}' на получение списка категорий");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        log.info("{}: Запрос к эндпоинту '{}' на получение категории с ID={}", categoryId);
        return categoryService.getCategoryById(categoryId);
    }
}