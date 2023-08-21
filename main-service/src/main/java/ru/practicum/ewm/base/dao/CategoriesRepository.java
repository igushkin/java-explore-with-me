package ru.practicum.ewm.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.base.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}
