package com.project.eat.order.orderItemOption;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemOptionService {

    private final OrderItemOptionRepository orderItemRepository;
}
