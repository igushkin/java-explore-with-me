package ru.practicum.ewm.common.dto.compilation;

import lombok.*;
import ru.practicum.ewm.common.dto.event.EventShortDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDto {

    private List<EventShortDto> events;
    private Long id;
    private boolean pinned;
    private String title;
}