package com.project.eat.member;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryEM {

    private final EntityManager em;

    public MemberVO_JPA findOne(String memberId) {
        return em.find(MemberVO_JPA.class, memberId);
    }
}

