package com.project.eat.order.orderItemOption;

import com.project.eat.item.ItemOption;
import com.project.eat.order.orderItem.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class OrderItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_option_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @ManyToOne
    @JoinColumn(name = "item_option_id", nullable = false)
    private ItemOption itemOption;
}
