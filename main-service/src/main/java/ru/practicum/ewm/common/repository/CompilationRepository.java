package ru.practicum.ewm.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.common.entity.Compilation;
import ru.practicum.ewm.common.util.MyPageRequest;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
   List<Compilation> findAllByPinned(Boolean pinned, MyPageRequest pageable);

}