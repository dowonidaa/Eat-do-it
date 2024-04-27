package com.project.eat.admin;

import com.project.eat.member.MemberDAO_JPA;
import com.project.eat.member.MemberVO_JPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

	@Autowired
	private AdminDAO_JPA jpa;

	@Autowired
	private MemberDAO_JPA member_jpa;


	public AdminVO_JPA insertOK(AdminVO_JPA vo) {
		return jpa.save(vo); //pk 즉 num값이 있으면 수정, 없으면 입력, dao재정의 필요없음
	}

	@Transactional
	public int deleteOK(AdminVO_JPA vo) {
		return jpa.deleteByAdminId(vo.getAdminId()); //함수커스텀
	}

	public List<AdminVO_JPA> selectAll() {
		return jpa.selectAll_JPQL();//JPQL
	}

	public List<AdminVO_JPA> selectAllPageBlock(int cpage, int pageBlock) {
		int startRow = (cpage - 1) * pageBlock + 1;
		
		return jpa.selectAllPageBlock(startRow-1, pageBlock);//네이티브쿼리사용 함수
	}

	public AdminVO_JPA selectOne(AdminVO_JPA vo) {
		return jpa.findByAdminId(vo.getAdminId());
	}

	public long getTotalRows() {
		return jpa.count();//내장함수 : dao에 재정의 안해도됨.
	}

	// adminId로 AdminVO_JPA 객체를 찾기
	public AdminVO_JPA findByAdminId(int adminId) {
		return jpa.findByAdminId(adminId); // 없는 경우 null 반환
	}


}
