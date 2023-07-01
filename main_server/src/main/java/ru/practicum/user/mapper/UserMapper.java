package ru.practicum.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User mapToUser(UserDto userDto) {
        return User.builder()
                   .id(userDto.getId())
                   .name(userDto.getName())
                   .email(userDto.getEmail())
                   .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                      .id(user.getId())
                      .name(user.getName())
                      .email(user.getEmail())
                      .build();
    }

    public static UserShortDto mapToUserShortDto(User user) {
        return UserShortDto.builder()
                           .id(user.getId())
                           .name(user.getName())
                           .build();
    }

    public static User mapToUser(NewUserRequest newUserRequest) {
        return User.builder()
                   .name(newUserRequest.getName())
                   .email(newUserRequest.getEmail())
                   .build();
    }
}