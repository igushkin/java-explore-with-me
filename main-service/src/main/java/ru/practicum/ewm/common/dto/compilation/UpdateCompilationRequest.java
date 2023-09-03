package ru.practicum.ewm.common.dto.compilation;

import lombok.*;
import ru.practicum.ewm.common.util.annotation.NotBlankNull;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @NotBlankNull
    @Size(min = 1, max = 50)
    private String title;
}