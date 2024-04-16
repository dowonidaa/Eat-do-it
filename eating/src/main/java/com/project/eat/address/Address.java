package com.project.eat.address;

import com.project.eat.member.MemberVO_JPA;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_Id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY ,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private MemberVO_JPA member;

    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;



}
