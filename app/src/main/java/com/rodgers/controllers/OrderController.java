package com.rodgers.controllers;

import com.rodgers.model.Order;
import com.rodgers.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Order> saveOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @GetMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Order> getOrder(@PathVariable Integer orderId) {
        return orderService.getOrder(orderId);
    }
}
