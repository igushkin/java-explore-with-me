package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.common.entity.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {
}