package ru.practicum.ewm.admin_access.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin_access.service.category.AdminCategoriesService;
import ru.practicum.ewm.common.dto.category.CategoryDto;
import ru.practicum.ewm.common.dto.category.NewCategoryDto;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    public final AdminCategoriesService adminCategoriesService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody @Valid NewCategoryDto dto) {
        log.info("Получен запрос POST /admin/categories c новой категорией: {}", dto.getName());
        return new ResponseEntity<>(adminCategoriesService.create(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable Long catId) {
        log.info("Получен запрос DELETE /admin/categories/{}", catId);
        adminCategoriesService.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@RequestBody @Valid NewCategoryDto dto,
                                              @PathVariable Long catId) {
        log.info("Получен запрос PATCH /admin/categories/{} на изменение категориии: {}", catId, dto.getName());
        return new ResponseEntity<>(adminCategoriesService.update(dto, catId), HttpStatus.OK);
    }
}