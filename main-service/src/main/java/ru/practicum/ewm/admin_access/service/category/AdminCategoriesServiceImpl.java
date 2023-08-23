package ru.practicum.ewm.admin_access.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.dto.Category.CategoryDto;
import ru.practicum.ewm.common.dto.Category.NewCategoryDto;
import ru.practicum.ewm.common.entity.Category;
import ru.practicum.ewm.common.exception.ConditionsNotMetException;
import ru.practicum.ewm.common.exception.ConflictException;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.common.mapper.CategoryMapper;
import ru.practicum.ewm.common.repository.CategoriesRepository;
import ru.practicum.ewm.common.repository.EventRepository;
import ru.practicum.ewm.common.util.UtilMergeProperty;

import javax.validation.ConstraintViolationException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminCategoriesServiceImpl implements AdminCategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto dto) {
        Category category = CategoryMapper.toEntity(dto);
        try {
            category = categoriesRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Add category: {}", category.getName());
        return CategoryMapper.toDto(category);
    }

    @Transactional
    @Override
    public void delete(Long catId) {
        try {
            categoriesRepository.deleteById(catId);
            log.info("Deleted category with id = {}", catId);
        } catch (ConstraintViolationException e) {
            throw new ConditionsNotMetException("The category is not empty");
        }
    }

    @Transactional
    @Override
    public CategoryDto update(NewCategoryDto dto, Long catId) {
        Category categoryUpdate = CategoryMapper.toEntity(dto);
        Category categoryTarget = getCategoryById(catId);

        try {
            UtilMergeProperty.copyProperties(categoryUpdate, categoryTarget);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }
        log.info("Update category: {}", categoryTarget.getName());
        return CategoryMapper.toDto(categoryTarget);
    }

    private Category getCategoryById(Long id) {
        final Category category = categoriesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category not found with id = %s", id)));
        log.info("Get category: {}", category.getName());
        return category;
    }

}