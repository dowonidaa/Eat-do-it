package com.project.eat.admin;

import com.project.eat.member.MemberVO_JPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdminDAO_JPA extends JpaRepository<AdminVO_JPA, Object> {

    //***jpa에 내장된 함수 또는 약속된 규칙에 맞게 정의한 함수.***
    public List<AdminVO_JPA> findAll();

    //***JPQL : @Query(객체를 테이블로 하는 쿼리)***
    @Query("select vo from AdminVO_JPA vo") //객체명은 대소분자 구분함.
    public List<AdminVO_JPA> selectAll_JPQL();

    //***네이티브쿼리(SQL) : @Query(nativeQuery=true,value="일반쿼리문") //복잡한쿼리:서브쿼리,조인
    //비밀번호가 너무길어서 10글자만 잘라서 반환함.
    @Query(nativeQuery=true,
            value="select * from admin limit ?1 , ?2")
    public List<AdminVO_JPA> selectAllPageBlock(Integer startRow, Integer pageBlock);



    //내장함수 커스텀
    public AdminVO_JPA findByAdminId(int adminId);

    public int deleteByAdminId(int adminId);

}
