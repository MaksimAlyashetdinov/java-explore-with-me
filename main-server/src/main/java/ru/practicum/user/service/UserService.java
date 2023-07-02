package ru.practicum.user.service;

import java.util.List;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.model.User;

public interface UserService {

    User addUser(NewUserRequest newUserRequest);

    List<User> getUsers(List<Integer> ids, int from, int size);

    void deleteUser(Integer id);
}