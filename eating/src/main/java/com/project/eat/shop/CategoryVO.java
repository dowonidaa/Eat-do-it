package com.project.eat.shop;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="category",uniqueConstraints = {
        @UniqueConstraint(
                columnNames= {"cate_id"}
        )
})
public class CategoryVO {
    @Id  //pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increament
    @Column(name="cate_id")
    private int cateId;

    @Column(name="cate_name",nullable = false)
    private String cateName;
}