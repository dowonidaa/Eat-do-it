package com.project.eat.shop;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="review",uniqueConstraints = {
        @UniqueConstraint(
                columnNames= {"review_id"}
        )
})
public class ReviewVO {

    @Id  //pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increament
    @Column(name="review_id")
    private int reviewId;

    @Column(name="user_id",nullable = false)
    private String userId;

    @Column(name="shop_id",nullable = false)
    private int shopId;

    private String shopName;

    @Column(name="review_star",nullable = false)
    private int reviewStar;

    @Column(name="review_coment")
    private String reviewComent;

    @Column(name="review_pic")
    private String reviewPic;

    @Column(name="created_at",insertable = false,
            columnDefinition = "DATETIME(0) DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;
}


