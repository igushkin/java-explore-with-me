package ru.practicum.explore_with_me.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explore_with_me.dto.UserDto;
import ru.practicum.explore_with_me.dto.UserShortDto;
import ru.practicum.explore_with_me.entity.User;

@Component
public class UserMapper {
    UserDto newUserRequestToUserDto(NewUserRequest newUserRequest);

    User userDtoToUser(UserDto userDto);

    public UserDto userToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    UserShortDto userDtoToUserShortDto(UserDto userDto);

}