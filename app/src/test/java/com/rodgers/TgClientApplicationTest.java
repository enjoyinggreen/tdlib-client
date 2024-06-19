package com.rodgers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TgClientApplicationTest {
    @Autowired
    private WebTestClient webTestClient;
    @Test
    @Order(1)
    void saveOrderTest() {
        webTestClient.post()
                .uri("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\"name\":\"John\"}"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":1,\"name\":\"John\"}");
    }
    @Test
    @Order(2)
    void getOrderTest() {
        webTestClient.get()
                .uri("/order/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody().json("{\"id\":1,\"name\":\"John\"}");
    }
}
