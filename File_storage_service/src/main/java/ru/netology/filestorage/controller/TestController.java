package ru.netology.filestorage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Test controller works! Application is running.";
    }

    @GetMapping("/test-env")
    public String testEnv() {
        return "Java version: " + System.getProperty("java.version");
    }
}
