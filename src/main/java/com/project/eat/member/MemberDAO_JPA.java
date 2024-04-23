package com.project.eat.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberDAO_JPA extends JpaRepository<MemberVO_JPA, Object>{

	//***jpa에 내장된 함수 또는 약속된 규칙에 맞게 정의한 함수.*** 
	public List<MemberVO_JPA> findAll();
	public List<MemberVO_JPA> findByOrderByNumDesc();
	public List<MemberVO_JPA> findByOrderByNumAsc();

	
	//***JPQL : @Query(객체를 테이블로 하는 쿼리)***
	@Query("select vo from MemberVO_JPA vo order by num desc") //객체명은 대소분자 구분함.
	public List<MemberVO_JPA> selectAll_JPQL();
	
	//***네이티브쿼리(SQL) : @Query(nativeQuery=true,value="일반쿼리문") //복잡한쿼리:서브쿼리,조인
	//비밀번호가 너무길어서 10글자만 잘라서 반환함.
	@Query(nativeQuery=true,
			value="select num,member_id,substring(pw,1,10) pw,name,tel,regdate,user_salt from member order by num desc limit ?1 , ?2")
	public List<MemberVO_JPA> selectAllPageBlock(Integer startRow, Integer pageBlock);
	
	//대소분자구분하는 검색
	public List<MemberVO_JPA> findByIdLike(String id);
	public List<MemberVO_JPA> findByNameLike(String name);
	public List<MemberVO_JPA> findByTelLike(String tel);
	
	//대소분자구분 없는 검색
	public List<MemberVO_JPA> findByIdIgnoreCaseLike(String id);
	public List<MemberVO_JPA> findByNameIgnoreCaseLike(String name);
	public List<MemberVO_JPA> findByTelIgnoreCaseLike(String tel);
	
	//대소분자구분 없는 검색 + 정렬
	public List<MemberVO_JPA> findByIdIgnoreCaseLikeOrderByNumDesc(String id);
	public List<MemberVO_JPA> findByNameIgnoreCaseLikeOrderByNumDesc(String name);
	public List<MemberVO_JPA> findByTelIgnoreCaseLikeOrderByNumDesc(String tel);
	
	//네이티브쿼리-searchListPageBlock
	@Query(nativeQuery=true,
			value="select num,member_id,substring(pw,1,10) pw,name,tel,regdate,user_salt from member "
					+ "where upper(member_id) like upper(?1) "
					+ "order by num desc limit ?2 , ?3")
	public List<MemberVO_JPA> searchListID_PAGE(String searchWord, int startRow, int pageBlock);
	
	@Query(nativeQuery=true,
			value="select num,member_id,substring(pw,1,10) pw,name,tel,regdate,user_salt from member "
					+ "where upper(name) like upper(?1) "
					+ "order by num desc limit ?2 , ?3")
	public List<MemberVO_JPA> searchListNAME_PAGE(String searchWord, int startRow, int pageBlock);
	
	@Query(nativeQuery=true,
			value="select count(*) total_rows from member  where upper(member_id) like upper(?1)")
	public long count_id(String searchWord);
	
	@Query(nativeQuery=true,
			value="select count(*) total_rows from member  where upper(name) like upper(?1)")
	public long count_name(String searchWord);
	
	//내장함수 커스텀
	public MemberVO_JPA findByNum(int num);

	//내장함수 커스텀 : 삭제시는 컨트롤러에 반드시 @Transactional
	public int deleteByNum(int num);

	//로그인
	@Query(nativeQuery=true,
			value="select * from member "
					+ "where member_id = ?1 and pw = ?2")
	public MemberVO_JPA findByLogin(String id, String pw);
	
	@Query(nativeQuery=true,
			value="select user_salt from member "
					+ "where member_id = ?1")
	public String findBySalt(String id);
	
	
	
}//end interface
