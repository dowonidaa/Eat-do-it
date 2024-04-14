package com.project.eat.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class MemberRestController {

	@Autowired
	private MemberService service;

//	@GetMapping("/m_insert")
//	public String m_insert(Model model) {
//		log.info("/m_insert...");
//
//		model.addAttribute("content", "thymeleaf/member/th_insert");
//		model.addAttribute("title", "회원가입");
//		return "thymeleaf/member/th_layout_main";
//	}
//
//	@PostMapping("/m_insertOK")
//	public String m_insertOK(MemberVO_JPA vo) {
//		log.info("/m_insertOK...");
//		log.info("vo:{}", vo);
//
//		MemberVO_JPA result = service.insertOK(vo);
//		log.info("result:{}", result);
//
//		if (result != null) {
//			return "redirect:m_selectAll";
//		} else {
//			return "redirect:m_insert";
//		}
//
//	}

	@GetMapping("/api/member/selectAll")
	public Map<String, Object>  m_selectAll(@RequestParam(name="cpage", defaultValue = "1") int cpage,
			@RequestParam(name="pageBlock",defaultValue = "5") int pageBlock) {
		log.info("/api/member/selectAll...");
		log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);

//		List<MemberVO_JPA> vos = service.selectAll();
		List<MemberVO_JPA> vos = service.selectAllPageBlock(cpage, pageBlock);

		// member테이블에 들어있는 모든회원수는 몇명?
		long total_rows = service.getTotalRows();
		log.info("total_rows:" + total_rows);

		long totalPageCount = 1;
		if (total_rows / pageBlock == 0) {
			totalPageCount = 1;
		} else if (total_rows % pageBlock == 0) {
			totalPageCount = total_rows / pageBlock;
		} else {
			totalPageCount = total_rows / pageBlock + 1;
		}
		// 페이지 링크 몇개?
		log.info("totalPageCount:" + totalPageCount);

		Map<String, Object> map =  new HashMap<>();
		map.put("vos", vos);
		map.put("totalPageCount", totalPageCount);
		
		return map;
	}

//	@GetMapping("/m_searchList")
//	public String m_searchList(@RequestParam(defaultValue = "1") int cpage,
//			@RequestParam(defaultValue = "5") int pageBlock, String searchKey, String searchWord, Model model) {
//		log.info("/m_searchList...");
//		log.info("searchKey:{}", searchKey);
//		log.info("searchWord:{}", searchWord);
//		log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);
//
////		List<MemberVO_JPA> vos = service.searchList(searchKey,searchWord);
//		List<MemberVO_JPA> vos = service.searchListPageBlock(searchKey, searchWord, cpage, pageBlock);
//
//		model.addAttribute("vos", vos);
//		
//
//		// 키워드검색 모든회원수는 몇명?
//		long total_rows = service.getSearchTotalRows(searchKey, searchWord);
//		log.info("total_rows:" + total_rows);
//
//		long totalPageCount = 1;
//		if (total_rows / pageBlock == 0) {
//			totalPageCount = 1;
//		} else if (total_rows % pageBlock == 0) {
//			totalPageCount = total_rows / pageBlock;
//		} else {
//			totalPageCount = total_rows / pageBlock + 1;
//		}
//		// 페이지 링크 몇개?
//		model.addAttribute("totalPageCount", totalPageCount);
////		model.addAttribute("totalPageCount", 10);//테스트용
//
//
//		model.addAttribute("content", "thymeleaf/member/th_selectAll");
//		model.addAttribute("title", "회원목록");
//		return "thymeleaf/member/th_layout_main";
//	}
//
//	// m_selectOne 을 에러없이 뷰처리해보세요.
//	@GetMapping("/m_selectOne")
//	public String m_selectOne(MemberVO_JPA vo, Model model) {
//		log.info("/m_selectOne...");
//		log.info("vo:{}", vo);
//
//		MemberVO_JPA vo2 = service.selectOne(vo);
//
//		model.addAttribute("vo2", vo2);
//
//		model.addAttribute("content", "thymeleaf/member/th_selectOne");
//		model.addAttribute("title", "회원정보 및 변경");
//		return "thymeleaf/member/th_layout_main";
//	}
//
//	// m_updateOK 만들기
//	@PostMapping("/m_updateOK")
//	public String m_updateOK(MemberVO_JPA vo) {
//		log.info("/m_updateOK...");
//		log.info("vo:{}", vo);
//
//		//수정일자 반영안하면 null값이 들어가는 것을 방지하기위해...
//		if(vo.getRegdate()==null) {
//			vo.setRegdate(new Date());
//		}
//		
//		MemberVO_JPA result = service.updateOK(vo);
//		log.info("result:{}", result);
//
//		return "redirect:m_selectAll";
//
//	}
//
//	@GetMapping("/m_delete")
//	public String m_delete(Model model) {
//		log.info("/m_delete...");
//
//		model.addAttribute("content", "thymeleaf/member/th_delete");
//		model.addAttribute("title", "회원삭제");
//		return "thymeleaf/member/th_layout_main";
//	}
//
//	// m_deleteOK 삭제시 반드시 @Transactional선언.
//	@Transactional
//	@PostMapping("/m_deleteOK")
//	public String m_deleteOK(MemberVO_JPA vo) {
//		log.info("/m_deleteOK...");
//		log.info("vo:{}", vo);
//
//		int result = service.deleteOK(vo);
//		log.info("result:{}", result);
//
//		return "redirect:m_selectAll";
//	}

}
