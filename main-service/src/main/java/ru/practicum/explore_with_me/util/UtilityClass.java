package ru.practicum.explore_with_me.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@lombok.experimental.UtilityClass
public class UtilityClass {
    public static PageRequest getPage(int from, int size, String sort, Sort.Direction direction) {
        Sort sortBy = Sort.by(direction, sort);
        return PageRequest.of((from / size), size, sortBy);
    }
}