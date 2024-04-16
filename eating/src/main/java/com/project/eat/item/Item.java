package com.project.eat.item;

import com.project.eat.shop.ShopVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopVO shop;

    private String itemName;
    private int itemPrice;
    private String itemUrl;

    @ColumnDefault("0")
    @Comment("인기 여부 1,0")
    private int popular;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String status;

    private String itemDescription;

    @OneToMany(mappedBy = "item")
    private List<ItemOption> itemOptions = new ArrayList<>();


}
