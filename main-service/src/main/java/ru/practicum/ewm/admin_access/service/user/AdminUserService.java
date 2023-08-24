package ru.practicum.ewm.admin_access.service.user;

import ru.practicum.ewm.common.dto.user.NewUserRequest;
import ru.practicum.ewm.common.dto.user.UserDto;

import java.util.List;

public interface AdminUserService {
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto save(NewUserRequest newUserRequest);

    void delete(Long userId);
}