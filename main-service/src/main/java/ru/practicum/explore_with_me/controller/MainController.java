package ru.practicum.explore_with_me.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    public ResponseEntity greet() {
        return ResponseEntity.ok("HI");
    }
}