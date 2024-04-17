package com.project.eat.address;

import com.project.eat.member.MemberVO_JPA;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_Id")
    private Long id;

//    @OneToOne(fetch = FetchType.LAZY ,cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "member_id")
//    private MemberVO_JPA member;

    @Column(name = "member_id", nullable = false)
    private String member;

    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Override
    public String toString() {
        return "주소: " + address + ", 사용자: " + member;
    }
}
