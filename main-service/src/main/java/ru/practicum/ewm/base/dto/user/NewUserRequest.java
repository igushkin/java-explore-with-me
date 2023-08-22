package ru.practicum.ewm.base.dto.user;

import lombok.*;
import ru.practicum.ewm.base.util.notblanknull.NotBlankNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewUserRequest {
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
    @Email
    @NotEmpty
    @Size(min = 6, max = 254)
    private String email;
}