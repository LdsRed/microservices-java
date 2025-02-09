package com.jlarcher.microservices.order.controller;


import com.jlarcher.microservices.order.dto.OrderRequest;
import com.jlarcher.microservices.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody OrderRequest request){
        orderService.placeOrder(request);
        return "Order Created Successfully";

    }
}
