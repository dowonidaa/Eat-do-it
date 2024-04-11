package com.project.eat.shop;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="menu",uniqueConstraints = {
        @UniqueConstraint(
                columnNames= {"menu_id"}
        )
})
public class MenuVO {
    @Id  //pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increament
    @Column(name="menu_id")
    private int menu_id;

    @Column(name="shop_name")
    private String shopName;

    @Column(name="menu_name",nullable = false)
    private String menuName;

    @Column(name="menu_desc")
    private String menuDesc;

    @Column(name="menu_price",nullable = false)
    private String menuPrice;

    @Column(name="menu_pic")
    private String menuPic;

    @Column(name="shop_id")
    private int shopId;

}
