package com.slalom.api;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    //test this via http://localhost:8080/greeting and http://localhost:8080/greeting?name=Kesha
    public ApiContainer greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new ApiContainer(counter.incrementAndGet(), String.format(template, name));
    }
}