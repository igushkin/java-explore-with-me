package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.CategoryDto;
import ru.practicum.explore_with_me.entity.Category;
import ru.practicum.explore_with_me.exception.AlreadyExistsException;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.CategoryMapper;
import ru.practicum.explore_with_me.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore_with_me.util.UtilityClass.getPage;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository repository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        try {
            Category category = repository.save(categoryMapper.categoryDtoToCategory(categoryDto));
            return categoryMapper.categoryToCategoryDto(category);
        } catch (Exception e) {
            throw new AlreadyExistsException("AlreadyExistsException");
        }
    }

    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = repository.findById(categoryDto.getId())
                .orElseThrow(() -> new NotFoundException("Категория с ID=" + categoryDto.getId() + " не найдена!"));

        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }

        category = repository.save(category);
        return categoryMapper.categoryToCategoryDto(category);
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ID=" + categoryId + " не найдена!"));
        repository.deleteById(categoryId);
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с ID=" + id + " не найдена!"));
        return CategoryMapper.categoryToCategoryDto(category);
    }

    public List<CategoryDto> getCategories(Integer from, Integer size) {
        Pageable page = getPage(from, size, "id", Sort.Direction.ASC);
        return repository.findAll(page)
                .stream()
                .map(CategoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

/*    public Category getCategory(Long categoryId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ID=" + categoryId + " не найдена!"));
        return category;
    }*/
}