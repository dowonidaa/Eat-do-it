package com.project.eat.order.orderItem;

import com.project.eat.item.Item;
import com.project.eat.order.Order;
import com.project.eat.order.orderItemOption.OrderItemOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false)@Comment("주문 음식 id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private int quantity;
    private int price;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.PERSIST)
    private List<OrderItemOption> orderItemOptions = new ArrayList<>();

    public void addOrderItemOption(OrderItemOption orderItemOption) {
        orderItemOptions.add(orderItemOption);
        orderItemOption.setOrderItem(this);

    }
}
