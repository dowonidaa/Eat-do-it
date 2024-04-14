package com.project.eat.member;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "MEMBER", uniqueConstraints = { @UniqueConstraint(columnNames = { "member_id" }) })
public class MemberVO_JPA {
    @Id // pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column(name = "num") // 컬럼이름 설정
    private int num;

//    @Column(columnDefinition = "DATETIME(0) default CURRENT_TIMESTAMP",insertable = false)
//    private Date created_at;

////    @Column(name = "email", nullable = true)
//    @Column(name = "email")
//    private String email;

    @Column(name = "member_id", nullable = false)
    private String id;

////    @Column(name = "member_status", nullable = false)
//    @Column(name = "member_status")
//    private String status;

    @Column(name = "name", nullable = false)
    private String name;

////    @Column(name = "nickname", nullable = false)
//    @Column(name = "nickname")
//    private String nickname;

    @Column(name = "pw", nullable = false)
    private String pw;

    @Column(name = "tel", nullable = false)
    private String tel;

    @Column(name = "user_salt")
    private String salt;

    @Column(columnDefinition = "DATETIME(0) default CURRENT_TIMESTAMP",insertable = false)
    private Date regdate;
}
