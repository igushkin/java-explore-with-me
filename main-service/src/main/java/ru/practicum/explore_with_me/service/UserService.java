package ru.practicum.explore_with_me.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore_with_me.dto.UserDto;
import ru.practicum.explore_with_me.entity.User;
import ru.practicum.explore_with_me.exception.NotFoundException;
import ru.practicum.explore_with_me.mapper.UserMapper;
import ru.practicum.explore_with_me.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public Collection<UserDto> getUsers(Long[] ids, Integer from, Integer size) {
        Pageable page = getPage(from, size, "id", Sort.Direction.ASC);
        List<User> users;
        if (ids != null) {
            users = repository.findByIdIsIn(Arrays.asList(ids), page);
        } else {
            users = repository.findAll(page).stream()
                    .collect(toList());
        }
        return users.stream()
                .map(mapper::userToUserDto)
                .collect(toList());
    }

    public UserDto getUserById(Long id) {
        return mapper.userToUserDto(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " was not found.")));
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        try {
            return mapper.userToUserDto(repository.save(mapper.userDtoToUser(userDto)));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistsException("Пользователь с E-mail=" +
                    userDto.getEmail() + " уже существует!");
        }
    }

    @Transactional
    public UserDto update(UserDto userDto, Long id) {
        if (userDto.getId() == null) {
            userDto.setId(id);
        }
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " was not found."));
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if ((userDto.getEmail() != null) && (userDto.getEmail() != user.getEmail())) {
            if (repository.findByEmail(userDto.getEmail())
                    .stream()
                    .filter(u -> u.getEmail().equals(userDto.getEmail()))
                    .allMatch(u -> u.getId().equals(userDto.getId()))) {
                user.setEmail(userDto.getEmail());
            } else {
                throw new AlreadyExistsException("Пользователь с E-mail=" + userDto.getEmail() + " уже существует!");
            }

        }
        return mapper.userToUserDto(repository.save(user));
    }

    @Transactional
    public void delete(Long userId) {
        try {
            repository.deleteById(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User with id=" + userId + " not found.");
        }
    }

    public User getUser(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found."));
    }
}