package com.project.eat.order;

import com.project.eat.member.MemberVO_JPA;
import com.project.eat.order.orderItem.OrderItem;
import com.project.eat.shop.ShopVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id",nullable = false)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private MemberVO_JPA member;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopVO shop;
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'READY'")
    private OrderStatus orderStatus;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime orderDate = LocalDateTime.now();
    private String orderAddress;
    private String orderAddressDetail;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private int orderPrice; // 배달비
    private String memberNotes; // 배달요구사항
    private String paymentMethod; // 결제방법
    private String paymentStatus; //결제상태
    private int discount;
    private String orderTel;
    private String tid;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);

    }



}
