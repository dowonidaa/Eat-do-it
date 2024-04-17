package com.project.eat.member;

import com.project.eat.address.Address;
import com.project.eat.cart.Cart;
import com.project.eat.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "member")
public class MemberVO_JPA {

    private int num;

    @Id
    @Column(name = "member_id")
    @Comment("회원 id")
    private String id;

    private String pw;

    @Comment("회원 이름")
    private String name;
    private String tel;
    private String nickname;
    private String email;

    @Column(name = "user_salt")
    private String salt;

    private Date regdate;

    @ColumnDefault("'회원'")
    private String memberStatus;

    @ColumnDefault("CURRENT_TIMESTAMP(6)")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "member", fetch = LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Coupon> coupons = new ArrayList<>();

    public void addCart(Cart cart) {
        this.setCart(cart);
        cart.setMember(this);
    }
}
