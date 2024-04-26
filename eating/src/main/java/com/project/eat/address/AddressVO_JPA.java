package com.project.eat.address;

import com.project.eat.member.MemberVO_JPA;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Address")
public class AddressVO_JPA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_Id")
    private Long aId;

//    @OneToOne(fetch = FetchType.LAZY ,cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "member_id")
//    private MemberVO_JPA member;

    @OneToOne(fetch = FetchType.LAZY) // 다대일 관계 설정
    @JoinColumn(name = "member_id") // 외래키 설정
    private MemberVO_JPA mId;

    private String address;

    private LocalDateTime createdAt=LocalDateTime.now();
    private LocalDateTime modifiedAt;

    @Override
    public String toString() {
        return "주소: " + address + ", 사용자: " + (mId != null ? mId.getId() : "null");
    }
}