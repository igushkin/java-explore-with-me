package ru.practicum.explore_with_me.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HitStatDto {
    private String app;
    private String uri;
    private Long hits;

/*    public HitStatDto(String a, String b, Long c) {

    }*/
}