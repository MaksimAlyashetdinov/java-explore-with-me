package ru.practicum.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequest {

    @NotNull
    @Email
    @Size(min = 6, max = 254)
    String email;
    @NotNull
    @Size(min = 2, max = 250)
    String name;
}