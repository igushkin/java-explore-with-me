package ru.practicum.explore_with_me.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}