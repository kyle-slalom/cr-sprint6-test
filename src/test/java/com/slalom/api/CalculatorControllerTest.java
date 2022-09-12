package com.slalom.api;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalculatorControllerTest {
    @Autowired
    private TestRestTemplate template;

    @Test
    public void getCalculator() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/calculator?number1=7&number2=2", String.class);
        assertThat(response.getBody()).isEqualTo("{\"id\":1,\"content\":\"The total is: 9!\"}");
    }
    @Test
    public void getCalculatorDefault() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/calculator", String.class);
        assertThat(response.getBody()).isEqualTo("{\"id\":2,\"content\":\"The total is: 0!\"}");
    }

}
