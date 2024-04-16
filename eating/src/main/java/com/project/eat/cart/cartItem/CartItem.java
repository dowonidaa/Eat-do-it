package com.project.eat.cart.cartItem;

import com.project.eat.cart.Cart;
import com.project.eat.cart.CartItemOption;
import com.project.eat.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
public class CartItem{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    @Comment("장바구니 음식 id")
    private Long id;



    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    @Comment("장바구니 id")
    private Cart cart;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @Comment("음식 id")
    private Item item;

    @Column(nullable = false)
    @Comment("장바구니 수량")
    private int quantity;

    @Column(nullable = false)
    @Comment("장바구니 가격")
    private int price;

    @OneToMany(mappedBy = "cartItem" ,cascade = CascadeType.REMOVE)
    private List<CartItemOption> cartItemOptions = new ArrayList<>();

    public void addCartItemOption(CartItemOption cartItemOption) {
        cartItemOptions.add(cartItemOption);
        cartItemOption.setCartItem(this);
    }

    public void cartPrice(){
        int optionPrice = 0;
        for (CartItemOption cartItemOption : cartItemOptions) {
           optionPrice += cartItemOption.getItemOption().getPrice();
        }

        this.setPrice((this.getItem().getItemPrice()+optionPrice )* this.getQuantity());
    }

}
