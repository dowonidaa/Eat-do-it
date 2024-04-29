package com.project.eat.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberDAO_JPA jpa;
	private final MemberRepositoryEM memberRepository;


	public boolean doesMemberExist(String id) {

		MemberVO_JPA not_exist = memberRepository.findOne(id); // 엔터티 조회

		if(not_exist == null){
			return true;
		} else {
			return false;
		}
	}

	public MemberVO_JPA insertOK(MemberVO_JPA vo) {
		return jpa.save(vo); //pk 즉 num값이 있으면 수정, 없으면 입력, dao재정의 필요없음
	}

	public MemberVO_JPA updateOK(MemberVO_JPA vo) {

		//MemberVO_JPA existingMember = jpa.findByNum(vo.getNum()); // 기존의 멤버 정보를 조회
		MemberVO_JPA existingMember = jpa.findById(vo.getId()); // 기존의 멤버 정보를 조회

		// 입력 받은 값과 기존 값 중에서 값이 존재하는 것을 우선적으로 선택
		existingMember.setId(vo.getId() != null ? vo.getId() : existingMember.getId());
		existingMember.setStatus(vo.getStatus() != null ? vo.getStatus() : existingMember.getStatus());
		existingMember.setName(vo.getName() != null ? vo.getName() : existingMember.getName());
		existingMember.setEmail(vo.getEmail() != null ? vo.getEmail() : existingMember.getEmail());
		existingMember.setNickname(vo.getNickname() != null ? vo.getNickname() : existingMember.getNickname());
		existingMember.setPw(vo.getPw() != null ? vo.getPw() : existingMember.getPw());
		existingMember.setTel(vo.getTel() != null ? vo.getTel() : existingMember.getTel());
		existingMember.setSalt(vo.getSalt() != null ? vo.getSalt() : existingMember.getSalt());

		// 기존의 멤버 정보를 저장하여 업데이트
		return jpa.save(existingMember);
	}

	public int deleteOK(MemberVO_JPA vo) {
		return jpa.deleteByNum(vo.getNum()); //함수커스텀
	}

	public List<MemberVO_JPA> selectAll() {
//		return jpa.findAll();
//		return jpa.findByOrderByNumDesc();//역정렬해주는 메소드를 jpa규칙에 따라 빌드 가능
//		return jpa.findByOrderByNumAsc();//순정렬해주는 메소드를 jpa규칙에 따라 빌드 가능
		return jpa.selectAll_JPQL();//JPQL
	}



	public List<MemberVO_JPA> selectAllPageBlock(int cpage, int pageBlock) {
		int startRow = (cpage - 1) * pageBlock + 1;
		
		return jpa.selectAllPageBlock(startRow-1, pageBlock);//네이티브쿼리사용 함수
	}

	public MemberVO_JPA selectOne(MemberVO_JPA vo) {
		return jpa.findByNum(vo.getNum());
	}
	public MemberVO_JPA selectOneById(MemberVO_JPA vo) {
		return jpa.findById(vo.getId());
	}
	public MemberVO_JPA selectOneByEmail(MemberVO_JPA vo) {
		return jpa.findByEmail(vo.getEmail());
	}
	
	public List<MemberVO_JPA> searchList(String searchKey, String searchWord) {
		
		//대소문자 구분함.
//		if(searchKey.equals("id")) {
//			return jpa.findByIdLike("%"+searchWord+"%");
//		}else if(searchKey.equals("name")) {
//			return jpa.findByNameLike("%"+searchWord+"%");
//		}else {
//			return jpa.findByTelLike("%"+searchWord+"%");
//		}
		
//		//대소문자 구분안함.
//		if(searchKey.equals("id")) {
//			return jpa.findByIdIgnoreCaseLike("%"+searchWord+"%");
//		}else if(searchKey.equals("name")) {
//			return jpa.findByNameIgnoreCaseLike("%"+searchWord+"%");
//		}else {
//			return jpa.findByTelIgnoreCaseLike("%"+searchWord+"%");
//		}
		//대소문자 구분안함.+ 정렬
		if(searchKey.equals("id")) {
			return jpa.findByIdIgnoreCaseLikeOrderByNumDesc("%"+searchWord+"%");
		}else if(searchKey.equals("name")) {
			return jpa.findByNameIgnoreCaseLikeOrderByNumDesc("%"+searchWord+"%");
		}else {
			return jpa.findByTelIgnoreCaseLikeOrderByNumDesc("%"+searchWord+"%");
		}
	}

	public List<MemberVO_JPA> searchListPageBlock(
			String searchKey, String searchWord, 
			int cpage, int pageBlock) {
		
		int startRow = (cpage - 1) * pageBlock + 1;
		
		if(searchKey.equals("id")) {
			return jpa.searchListID_PAGE("%"+searchWord+"%",startRow-1,pageBlock);//네이티브 쿼리
		}else {
			return jpa.searchListNAME_PAGE("%"+searchWord+"%",startRow-1,pageBlock);//네이티브 쿼리
		}
	}

	public long getTotalRows() {
		return jpa.count();//내장함수 : dao에 재정의 안해도됨.
	}

	public long getSearchTotalRows(String searchKey, String searchWord) {
		
		if(searchKey.equals("id")) {
			return jpa.count_id("%"+searchWord+"%");
		}else {
			return jpa.count_name("%"+searchWord+"%");
		}
	}

	public MemberVO_JPA loginOK(MemberVO_JPA vo) {
		return jpa.findByLogin(vo.getId(),vo.getPw());
	}

	public String getSalt(MemberVO_JPA vo) {
		return jpa.findBySalt(vo.getId());
	}



	@Transactional(readOnly = true)
	public MemberVO_JPA findOne(String memberId) {
		return memberRepository.findOne(memberId);
	}

	public int findNumByMemberId(String memberId) {
		return jpa.findNumByMemberId(memberId);
	}
}
