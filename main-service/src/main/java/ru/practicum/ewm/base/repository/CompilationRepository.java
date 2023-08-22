package ru.practicum.ewm.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.base.entity.Compilation;
import ru.practicum.ewm.base.util.page.MyPageRequest;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
   List<Compilation> findAllByPinned(Boolean pinned, MyPageRequest pageable);

}