package com.project.eat.shop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class CategoryVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private Long id;

    private String cateName;

    @OneToMany(mappedBy = "category")
    private List<ShopVO> shops = new ArrayList<>();

}
