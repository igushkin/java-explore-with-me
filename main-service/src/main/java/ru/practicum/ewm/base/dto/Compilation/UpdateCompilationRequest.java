package ru.practicum.ewm.base.dto.Compilation;

import lombok.*;
import ru.practicum.ewm.base.util.notblanknull.NotBlankNull;

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