package ru.practicum.user.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User addUser(NewUserRequest newUserRequest) {
        validateUser(newUserRequest);
        log.info("Add new user: name - {}, email - {}", newUserRequest.getName(), newUserRequest.getEmail());
        return userRepository.save(UserMapper.mapToUser(newUserRequest));
    }

    @Override
    public List<User> getUsers(List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        log.info("Get users with id {}", ids);
        List<User> result = new ArrayList<>();
        if (ids == null) {
            result = userRepository.searchWithoutIds(pageable);
        } else {
            result = userRepository.findAllByIdIn(ids, pageable);
        }
        return result;
    }

    @Override
    public void deleteUser(Integer id) {
        containsUser(id);
        User user = userRepository.findById(id).get();
        log.info("Delete user with id {}", id);
        userRepository.delete(user);
    }

    private void containsUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User with id " + id + " not found.");
        }
    }

    private void validateUser(NewUserRequest newUserRequest) {
        if (newUserRequest.getName().isBlank() || newUserRequest.getEmail().isBlank()) {
            throw new ValidationException("User name and email can't be empty.");
        }
        if (userRepository.findByName(newUserRequest.getName()) != null) {
            throw new ConflictException("User with name " + newUserRequest.getName() + " already exists.");
        }
    }
}