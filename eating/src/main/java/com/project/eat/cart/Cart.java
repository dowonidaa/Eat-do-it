package com.project.eat.cart;

import com.project.eat.cart.cartItem.CartItem;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.order.OrderType;
import com.project.eat.shop.ShopVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;


@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id", nullable = false)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberVO_JPA member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shop_id")
    private ShopVO shop;

    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    private int totalPrice;

    @OneToMany(mappedBy = "cart" , cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setCart(this);
    }

    public void totalPrice() {
        int price = 0;
        for (CartItem cartItem : cartItems) {
            price  += cartItem.getPrice();
        }
        this.setTotalPrice(price);
    }

}
