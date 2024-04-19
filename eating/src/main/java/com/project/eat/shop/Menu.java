package com.project.eat.shop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String menuName;
    private String menuDesc;
    private String menuPrice;
    private String menuPic;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private ShopVO shop;
}
