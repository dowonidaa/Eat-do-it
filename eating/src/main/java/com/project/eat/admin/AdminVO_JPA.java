package com.project.eat.admin;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ADMIN", uniqueConstraints = { @UniqueConstraint(columnNames = { "admin_id" }) })
public class AdminVO_JPA {

    @Id  //pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increament
    @Column(name="admin_id")//컬럼이름 설정
    private int adminId;


    @Column(name="shop_id")//컬럼이름 설정
    private Long shopId;

    @Column(name="shop_name",nullable = false)
    private String shopName;

    @Column(name="star_avg")
    private String starAvg;

    @Column(name="review_count")
    private int reviewCount;

    @Column(name="delivery_time",nullable = false)
    private String deliveryTime;

    @Column(name="delivery_price")
    private int deliveryPrice;

    @Column(name="run_time")
    private String runTime;

    @Column(name="shop_tel",nullable = false)
    private String shopTel;

    @Column(name="shop_addr",nullable = false)
    private String shopAddr;

    @Column(name="min_price")
    private String minPrice;

    @Column(name="tag")
    private String tag;

    @Column(name="cate_id",nullable = false)
    private int cateId;

    @Column(name="shop_thum")
    private String shopThum;

    @Column(name="min_price_int")
    private Integer minPriceInt;
}
