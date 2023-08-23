package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Builder
public class CommentCreateDto {

    @NotBlank
    @Size(min = 1, max = 7000)
    private final String text;
}
