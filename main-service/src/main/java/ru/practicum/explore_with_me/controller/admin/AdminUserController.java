package ru.practicum.explore_with_me.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    //private final UserService userService;
    //private final UserMapper userMapper;

    @GetMapping
    public Collection<UserDto> getUsers(@RequestParam(value = "ids", required = false) Long[] ids,
                                        @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                        @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("{}: Запрос к эндпоинту '{}' на получение списка пользователей",
                request.getRemoteAddr(),
                request.getRequestURI());
        return userService.getUsers(ids, from, size);
    }

    @PostMapping()
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("{}: Запрос к эндпоинту '{}' на добавление пользователя {}", newUserRequest.toString());
        return userService.create(userMapper.newUserRequestToUserDto(newUserRequest));
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId, HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}' на удаление пользователя с ID={}", userId);
        userService.delete(userId);
    }
}