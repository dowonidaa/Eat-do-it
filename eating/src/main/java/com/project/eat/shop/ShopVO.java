package com.project.eat.shop;

import com.project.eat.cart.Cart;
import com.project.eat.item.Item;
import com.project.eat.member.Coupon;
import com.project.eat.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "shop")
public class ShopVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shopName;

    @ColumnDefault("0")
    private Double starAvg;

    @ColumnDefault("0")
    private Integer reviewCount;

    private String deliveryTime;
    private String runTime;
    private String shopTel;
    private String shopAddr;
    private String minPrice;
    private String tag;
    private Integer deliveryPrice;
    private Integer minPriceInt;

    @ColumnDefault("'default.png'")
    private String shopThum;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    private CategoryVO category;

    @OneToMany(mappedBy = "shop")
    private List<Coupon> coupons = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Cart> carts = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<Menu> menus = new ArrayList<>();
}
