package com.jlarcher.microservices.order.service;

import com.jlarcher.microservices.order.client.InventoryClient;
import com.jlarcher.microservices.order.dto.OrderRequest;
import com.jlarcher.microservices.order.model.Order;
import com.jlarcher.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    public void placeOrder(OrderRequest orderRequest){

        var isInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (isInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            order.setSkuCode(orderRequest.skuCode());
            orderRepository.save(order);
        }else {
            throw new RuntimeException("Product with skuCode " + orderRequest.skuCode() + " is out of Stock");
        }
    }
}
