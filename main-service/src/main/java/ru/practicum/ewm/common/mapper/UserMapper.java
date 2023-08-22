package ru.practicum.ewm.common.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.common.dto.user.NewUserRequest;
import ru.practicum.ewm.common.dto.user.UserDto;
import ru.practicum.ewm.common.dto.user.UserShortDto;
import ru.practicum.ewm.common.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public final class UserMapper {

    public static User toEntity(NewUserRequest dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public static UserDto toUserDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User entity) {
        return UserShortDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static List<UserDto> toUserDtoList(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

}