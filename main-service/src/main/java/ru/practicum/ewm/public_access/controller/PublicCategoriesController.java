package ru.practicum.ewm.public_access.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.common.dto.category.CategoryDto;
import ru.practicum.ewm.public_access.service.category.PublicCategoriesService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/categories")
public class PublicCategoriesController {

    public final PublicCategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        log.info("Получен запрос GET /categories c параметрами: from = {}, size = {}", from, size);
        return new ResponseEntity<>(categoriesService.getAll(from, size), HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> get(@PathVariable Long catId) {
        log.info("Получен запрос GET /categories/{}", catId);
        return new ResponseEntity<>(categoriesService.get(catId), HttpStatus.OK);
    }
}