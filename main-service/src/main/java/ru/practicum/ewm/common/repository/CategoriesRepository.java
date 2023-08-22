package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.common.entity.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}