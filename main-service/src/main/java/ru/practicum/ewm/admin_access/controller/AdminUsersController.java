package ru.practicum.ewm.admin_access.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin_access.service.user.AdminUserService;
import ru.practicum.ewm.common.dto.user.NewUserRequest;
import ru.practicum.ewm.common.dto.user.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
@Validated
public class AdminUsersController {

    private final AdminUserService adminUserService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(@RequestParam(required = false) List<Long> ids,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получен запрос GET /admin/users");
        return new ResponseEntity<>(adminUserService.getAll(ids, from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("Получен запрос POST /admin/users c новым пользователем: {}", newUserRequest.getEmail());
        return new ResponseEntity<>(adminUserService.save(newUserRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        log.info("Получен запрос DELETE /admin/users/{}", userId);
        adminUserService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}