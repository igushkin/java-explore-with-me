package ru.practicum.ewm.base.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.EventSearchCriteria;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class EventCriteriaRepositoryImpl implements EventCriteriaRepository {

    private final EntityManager entityManager;

    private final CriteriaBuilder criteriaBuilder;

    public EventCriteriaRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Event> findAllWithFilters(Pageable pageable, EventSearchCriteria eventSearchCriteria) {

        CriteriaQuery<Event> criteriaQuery = criteriaBuilder.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        Predicate predicate = getPredicate(eventSearchCriteria, eventRoot);
        criteriaQuery.where(predicate);

        if (pageable.getSort().isUnsorted()) {
            criteriaQuery.orderBy(criteriaBuilder.desc(eventRoot.get("createdOn")));
        }

        TypedQuery<Event> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Event> events = typedQuery.getResultList();

        return new PageImpl<>(events);
    }


    private Predicate getPredicate(EventSearchCriteria criteria, Root<Event> eventRoot) {
        List<Predicate> predicates = new ArrayList<>();
        Predicate annotationPredicate = null;
        if (Objects.nonNull(criteria.getText())) {
            annotationPredicate = criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get("annotation")),
                    "%" + criteria.getText().toLowerCase() + "%");
        }
        if (Objects.nonNull(criteria.getText()) && annotationPredicate == null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get("description")),
                    "%" + criteria.getText().toLowerCase() + "%"));
        } else if (Objects.nonNull(criteria.getText())) {
            Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(eventRoot.get("description")),
                    "%" + criteria.getText().toLowerCase() + "%");
            predicates.add(criteriaBuilder.or(annotationPredicate, descriptionPredicate));
        }

        if (criteria.getCategories() != null && !criteria.getCategories().isEmpty()) {
            Join<Event, Category> categoryJoin = eventRoot.join("category");
            predicates.add(categoryJoin.get("id").in(criteria.getCategories()));
        }
        if (criteria.getPaid() != null && criteria.getPaid().equals(Boolean.TRUE)) {
            predicates.add(criteriaBuilder.equal(eventRoot.get("paid"), criteria.getPaid()));
        }
        if (criteria.getRangeStart() != null || criteria.getRangeEnd() != null) {
            LocalDateTime rangeStart = criteria.getRangeStart() != null
                    ? criteria.getRangeStart()
                    : LocalDateTime.MIN;
            LocalDateTime rangeEnd = criteria.getRangeEnd() != null
                    ? criteria.getRangeEnd()
                    : LocalDateTime.MAX;
            predicates.add(criteriaBuilder.between(eventRoot.get("date"), rangeStart, rangeEnd));
        } else {
            predicates.add(criteriaBuilder.between(eventRoot.get("date"), LocalDateTime.now(), LocalDateTime.now().plusYears(100)));
        }

        if (criteria.getOnlyAvailable() != null && criteria.getOnlyAvailable()) {
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.isNull(eventRoot.get("participantLimit")),
                    criteriaBuilder.greaterThan(
                            criteriaBuilder.diff(eventRoot.get("participantLimit"), eventRoot.get("confirmedRequests")),
                            0L
                    )
            ));
        }
        return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    }

}
