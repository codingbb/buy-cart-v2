package com.example.buyer.orderItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class OrderItemController {
    private final OrderItemService orderItemService;

}
