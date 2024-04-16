package com.project.eat.cart;

import com.project.eat.cart.cartItem.CartItem;
import com.project.eat.item.ItemOption;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class CartItemOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_option_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_option_id", nullable = false)
    private ItemOption itemOption;
}
