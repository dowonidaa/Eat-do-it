package com.project.eat.address;

import com.project.eat.member.MemberVO_JPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressDAO_JPA extends JpaRepository<AddressVO_JPA, Object>{

	//***jpa에 내장된 함수 또는 약속된 규칙에 맞게 정의한 함수.*** 
	public List<AddressVO_JPA> findAll();

	@Query("select vo from AddressVO_JPA vo") //객체명은 대소분자 구분함.
	public List<AddressVO_JPA> selectAll_JPQL();

	public AddressVO_JPA findBymId(MemberVO_JPA mId);

	@Query(nativeQuery=true,
			value="select address from address where member_id = ?1 ")
	String findAddressBymId(String memId);
}//end interface