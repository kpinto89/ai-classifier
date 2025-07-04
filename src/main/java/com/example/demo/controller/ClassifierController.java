package com.example.demo.controller;

import com.example.demo.service.TextClassifierService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ClassifierController {

    private final TextClassifierService classifierService;

    public ClassifierController(TextClassifierService classifierService) {
        this.classifierService = classifierService;
    }

    @GetMapping("/classify")
    public String classify(@RequestParam("text") String text) {
        return classifierService.classify(text);
    }
}
