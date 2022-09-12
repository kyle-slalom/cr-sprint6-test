package com.slalom.api;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    private static final String template = "The total is: %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/calculator")
    //test this via http://localhost:8080/calculator and http://localhost:8080/calculator?number1=7
    public ApiContainer calculator(@RequestParam(value = "number1", defaultValue = "0") Integer number1, @RequestParam(value = "number2", defaultValue = "0") Integer number2 ) {
        return new ApiContainer(counter.incrementAndGet(), String.format(template, number1 + number2));
    }
}