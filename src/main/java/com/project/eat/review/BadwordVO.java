package com.project.eat.review;

import com.project.eat.shop.ShopVO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="bad_word",uniqueConstraints = {
        @UniqueConstraint(
                columnNames= {"num"}
        )
})
public class BadwordVO {

    @Id  //pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto increament
    @Column(name="num")
    private int num;


    // 금지어
    @Column(name="bad_word")
    private String badWord;



}


