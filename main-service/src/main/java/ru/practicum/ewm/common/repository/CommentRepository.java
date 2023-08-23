package ru.practicum.ewm.common.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.common.entity.Comment;
import ru.practicum.ewm.common.util.MyPageRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndCommentatorId(Long id, Long commentatorId);

    Long deleteByIdAndAndCommentatorId(Long id, Long commentatorId);

    List<Comment> findByEventId(Long eventId, MyPageRequest pageable);
}