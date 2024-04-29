package com.project.eat.review;

import com.project.eat.member.MemberVO_JPA;
import com.project.eat.order.Order;
import com.project.eat.shop.ShopVO;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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

    //테이블:member 의 pk: num=>member pk:member_id 변경 : member fk설정 안하는것으로함
    @Column(name = "user_id")
    private int userId;

    @Column(name = "shop_id", insertable = false, updatable = false)
    private Long shopId;// 외래 키

    @OneToOne(targetEntity = ShopVO.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id") //테이블:shop 1대1 관계
    private ShopVO shop;
//    @Column(name = "shop_id")
//    private Long shopId;

    @Column(name="review_star",nullable = false)
    private int reviewStar;

    @Column(name="review_coment")
    private String reviewComent;

    @Column(name="review_pic")
    private String reviewPic;

    @Column(name="created_at",insertable = false,
            columnDefinition = "DATETIME(0) DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    // 유저명
    @Column(name="name")
    private String name;

    // 가게명
    @Column(name="shop_name")
    private String shopName;

    @OneToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public void setShop(ShopVO shop) {

        this.shop = shop;
        // ShopVO를 참조하는 필드를 설정하면서, 외래 키 값도 설정할 수 있음
        if (shop != null) {
            this.shopId = shop.getShopId(); // ShopVO의 기본키 값을 외래 키에 설정
        }
    }

}


