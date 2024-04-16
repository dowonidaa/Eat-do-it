package com.project.eat.member;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryEM {

    private final EntityManager em;

    public MemberVO_JPA findOne(String memberId) {
        return em.createQuery("select m from MemberVO_JPA m where m.id = :memberId", MemberVO_JPA.class)
                .setParameter("memberId", memberId)
                .getSingleResult();

    }


}

