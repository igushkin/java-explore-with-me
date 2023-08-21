package ru.practicum.ewm.base.dto.Compilation;

import lombok.*;
import ru.practicum.ewm.base.util.notblanknull.NotBlankNull;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;
    @NotBlankNull
    @Size(max = 128)
    private String title;
}
