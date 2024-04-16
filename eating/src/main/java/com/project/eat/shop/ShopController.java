package com.project.eat.shop;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

@Controller
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private GetUserAddrWithUserId getUserAddrWithUserId;

    // 음식점 메인 페이지
    @GetMapping("/shop_main")
    public String shop_main(){

        return "thymeleaf/shop/shopMainPage";
    }

    // 전체 음식점조회(테스트용)
    @GetMapping("/shop_selectAll")
    public String slelctAll(Model model){
        List<ShopVO> vos = shopService.selectAll();
        log.info("shop list vos in controller :  "+vos);
        model.addAttribute("vos", vos);
        return "thymeleaf/shop/shopListMain";
    }

    // 전체 음식점조회- 페이징처리 로그인하지 않았을경우
    @GetMapping("/shop_selectPageBlockAll")
    public String selectAllPageBlock(@RequestParam(name = "cpage", defaultValue = "1") int cpage,
                                     @RequestParam(name = "pageBlock", defaultValue = "10") int pageBlock,
                                     @RequestParam(name = "userId", defaultValue = "") String userId,
                                     Model model) {
        log.info("userId:{}", userId);

        // 로그인 안한, userId 없을 경우 => 전체조회
        List<ShopVO> vos = shopService.selectAllPageBlock(cpage, pageBlock);
        log.info("shop list vos in controller :  "+vos);


        // shop테이블에 들어있는 모든음식점수는 몇개?
        long total_rows = shopService.getTotalRows();
        log.info("total_rows:" + total_rows);

        // 로그인성공, userId 있을 경우 => 등록된 집주소 조회
        String userAddr = getUserAddrWithUserId.AddrGu(userId);
        log.info("유저 집주소 조회 :: userAddr:{}", userAddr);
        if(userId != null && !userId.isEmpty() && userAddr != null && !userAddr.isEmpty()){
            log.info("null도 아니고 그리고 엠티도 아니다 !!! 로그인성공이며 주소가 있다 ");

            // 로그인성공-userId집주소 주변 음식점 종 갯수
            total_rows = shopService.getTotalRowswithContainingGu(userAddr);
            log.info("로그인성공 집주변 음식점 총갯수 total_rows:" + total_rows);

            //로그인성공-userId집주소 주변 음식점 조회
            vos = shopService.findAllByShopAddrContainingPageBlock(userAddr, cpage, pageBlock);
            log.info("userId집주소 주변 음식점 조회 vos:{} ", vos);
        }

        // 결과값 없을 경우, sorry페이지 이동 및 홈버튼 제공
        if(total_rows<=0){
            return "thymeleaf/shop/sorry";
        }


        // 페이징처리계산식
        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }

        // 페이지처리 화면단에 전달
        log.info("totalPageCount:" + totalPageCount);
        model.addAttribute("totalPageCount", totalPageCount);

        model.addAttribute("vos", vos);


        return "thymeleaf/shop/shopListMain";
    }

    // 서치 결과: 해당 음식점들 조회 출력 :form input post방식
    @PostMapping("/shop_searchWord")
    public String shop_search(@RequestParam(name = "cpage", defaultValue = "1") int cpage,
                              @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
                              @RequestParam("searchWord") String searchWord,
                              @RequestParam(name = "userId", defaultValue = "") String userId,
                              Model model) {
        log.info("/shop_searchWord...");
        log.info("searchWord:{}", searchWord);
        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);

        // 로그인 없이 전체 음식점 대상 서치
        // List<ShopVO> vos = shopService.searchList(searchWord); //페이징처리없이 테스트용
        List<ShopVO> vos = shopService.searchListPageBlock(searchWord,cpage, pageBlock);

        // 키워드 검색한 shop테이블에 들어있는 getSearchTotalRows 몇개?
        long total_rows = shopService.getSearchTotalRows(searchWord);
        log.info("total_rows:" + total_rows);

        // 로그인성공, userId 있을 경우 => 등록된 집주소 조회
        String userAddr = getUserAddrWithUserId.AddrGu(userId);
        log.info("유저 집주소 조회 :: userAddr:{}", userAddr);
        if(userId != null && !userId.isEmpty() && userAddr != null && !userAddr.isEmpty()){
            log.info("null도 아니고 그리고 엠티도 아니다 !!! 로그인성공이며 주소가 있다 ");

            // 로그인성공-userId집주소 주변 음식점 + 검색키워드  => 총 갯수 조회
            total_rows = shopService.countAddrwithGuAndContaingSearchKey(userAddr, searchWord);
            log.info("로그인성공 집주변 음식점+검색키워드 총갯수 total_rows:" + total_rows);

            //로그인성공-userId집주소 주변 음식점 조회 + 검색키워드
            vos = shopService.searchListPageBlockMyAddr(userAddr, searchWord, cpage, pageBlock);
            log.info("userId집주소 주변 음식점 조회+검색키워드 vos:{} ", vos);
        }

        // 로그인성공, userId 있을 경우 => 등록된 집주소 조회
        userAddr = getUserAddrWithUserId.AddrGu(userId);
        log.info("유저 집주소 조회 :: userAddr:{}", userAddr);
        if(userId != null && !userId.isEmpty() && userAddr != null && !userAddr.isEmpty()){
            log.info("null도 아니고 그리고 엠티도 아니다 !!! 로그인성공이며 주소가 있다 ");

            // 로그인성공-userId집주소 주변 음식점 + 검색키워드  => 총 갯수 조회
            total_rows = shopService.countAddrwithGuAndContaingSearchKey(userAddr, searchWord);
            log.info("로그인성공 집주변 음식점+검색키워드 총갯수 total_rows:" + total_rows);

            //로그인성공-userId집주소 주변 음식점 조회 + 검색키워드
            vos = shopService.searchListPageBlockMyAddr(userAddr, searchWord, cpage, pageBlock);
            log.info("userId집주소 주변 음식점 조회+검색키워드 vos:{} ", vos);

            log.info("로그인 성공 ! 유저아이디 확인~~~~~~~~~~~~유저 집주소 조회 :: userId:{}", userId);
            model.addAttribute("userId", userId);
        }
        
        // 결과값 없을 경우, sorry페이지 이동 및 홈버튼 제공
        if(total_rows<=0){
            return "thymeleaf/shop/sorry";
        }

        // 페이징처리계산식
        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }

        // 페이지처리 화면단에 전달
        log.info("totalPageCount:" + totalPageCount);
        model.addAttribute("totalPageCount", totalPageCount);

        model.addAttribute("vos", vos);

//        model.addAttribute("userId", null);


        return "thymeleaf/shop/shopListMain";
    }

    // 서치 결과: 해당 음식점들 조회 출력 : 페이지네이션 이동
    @GetMapping("/shop_search")
    public String shop_search2(@RequestParam(name = "cpage", defaultValue = "1") int cpage,
                               @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
                               @RequestParam("searchWord") String searchWord,
                               @RequestParam(name = "userId", defaultValue = "") String userId,
                               Model model) {
        log.info("/shop_search2...");
        log.info("searchWord:{}", searchWord);
        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);

        List<ShopVO> vos = shopService.searchListPageBlock(searchWord,cpage, pageBlock);

        // 키워드 검색한 shop테이블에 들어있는 getSearchTotalRows 몇개?
        long total_rows = shopService.getSearchTotalRows(searchWord);
        log.info("total_rows:" + total_rows);


        // 로그인성공, userId 있을 경우 => 등록된 집주소 조회
        String userAddr = getUserAddrWithUserId.AddrGu(userId);
        log.info("유저 집주소 조회 :: userAddr:{}", userAddr);
        if(userId != null && !userId.isEmpty() && userAddr != null && !userAddr.isEmpty()){
            log.info("null도 아니고 그리고 엠티도 아니다 !!! 로그인성공이며 주소가 있다 ");

            // 로그인성공-userId집주소 주변 음식점 + 검색키워드  => 총 갯수 조회
            total_rows = shopService.countAddrwithGuAndContaingSearchKey(userAddr, searchWord);
            log.info("로그인성공 집주변 음식점+검색키워드 총갯수 total_rows:" + total_rows);

            //로그인성공-userId집주소 주변 음식점 조회 + 검색키워드
            vos = shopService.searchListPageBlockMyAddr(userAddr, searchWord, cpage, pageBlock);
            log.info("userId집주소 주변 음식점 조회+검색키워드 vos:{} ", vos);

            log.info("로그인 성공 ! 유저아이디 확인~~~~~~~~~~~~유저 집주소 조회 :: userId:{}", userId);
            model.addAttribute("userId", userId);
        }


        // 결과값 없을 경우, sorry페이지 이동 및 홈버튼 제공
        if(total_rows<=0){
            return "thymeleaf/shop/sorry";
        }

        // 페이징처리계산식
        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }

        // 페이지처리 화면단에 전달
        log.info("totalPageCount:" + totalPageCount);
        model.addAttribute("totalPageCount", totalPageCount);

        model.addAttribute("vos", vos);

//        model.addAttribute("userId", null);

        return "thymeleaf/shop/shopListMain";
    }

    // 카테고리별 버튼 클릭 음식점 조회(shopMainPage에 버튼으로 링크)
    @GetMapping("/categoryList.do")
    public String categoryList(@RequestParam(name = "cpage", defaultValue = "1") int cpage,
                               @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
                               @RequestParam("cateId") int cateId,
                               @RequestParam(name = "userId", defaultValue = "") String userId,
                               Model model){
        log.info("shop list vos in controller cateId :  "+cateId);

        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);
        log.info("카테고리 !!! userId : {},", userId);

        // 로그인 안한 경우
        List<ShopVO> vos = shopService.selectListByCategoryPageBlock(cateId,cpage, pageBlock);
//        List<ShopVO> vos = shopService.selectListByCategory(cateId); //페이지처리없음
        log.info("selectListByCategory list vos in controller :  "+vos);


        // shop테이블에 카테고리별 들어있는 모든음식점수는 몇개?
        long total_rows = shopService.getTotalRowsByCategory(cateId);
        log.info("total_rows:" + total_rows);


        // 로그인성공, userId 있을 경우 => 등록된 집주소 조회
        String userAddr = getUserAddrWithUserId.AddrGu(userId);
        log.info("유저 집주소 조회 :: userAddr:{}", userAddr);
        if(userId != null && !userId.isEmpty() && userAddr != null && !userAddr.isEmpty()){
            log.info("null도 아니고 그리고 엠티도 아니다 !!! 로그인성공이며 주소가 있다 ");
            log.info("로그인성공 카테고리별 !  cateId:{},  ", cateId);

            // 로그인성공-userId집주소 주변 음식점 + 카테고리별  => 총 갯수 조회
            total_rows = shopService.countAddrwithGuAndContaingCateId(userAddr, cateId);
            log.info("로그인성공 집주변 음식점+카테고리별 총갯수 total_rows:" + total_rows);
            log.info("로그인성공 카테고리별 로우갯수 !  total_rows:{},  ", total_rows);

            //로그인성공-userId집주소 주변 음식점 조회 + 카테고리별
            vos = shopService.cateIdListPageBlockMyAddr(userAddr, cateId, cpage, pageBlock);
            log.info("userId집주소 주변 음식점 조회+카테고리별 vos:{} ", vos);

            log.info("로그인 성공 ! 유저아이디 확인~~~~~~~~~~~~유저 집주소 조회 :: userId:{}", userId);
            model.addAttribute("userId", userId);
        }



        // 결과값 없을 경우, sorry페이지 이동 및 홈버튼 제공
        if(total_rows<=0){
            return "thymeleaf/shop/sorry";
        }

        // 페이징처리계산식
        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }

        // 페이지처리 화면단에 전달
        log.info("totalPageCount:" + totalPageCount);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("vos", vos);
        return "thymeleaf/shop/shopListMain";
    }

    // 내위치확인 카카오맵 html 페이지로 이동(shopMainPage에 버튼으로 링크)
    @GetMapping("/shopMainwithMyPosition")
    public String shopMainwithMyPosition(){
        log.info("shop list vos in controller cateId");
        return "thymeleaf/shop/shopMainwithMyPosition";
    }

    // 카카오맵api 내주변맛집 조회
    @PostMapping("/shop_mypositioncheckok")
    public String shop_mypositioncheckok(@RequestParam(name = "cpage", defaultValue = "1") int cpage,
                                    @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
            @RequestParam("mypositionaddr") String mypositionaddr, Model model) {

        log.info("/shop_mypositioncheckok...");
        log.info("mypositionaddr:{}", mypositionaddr);

        String[] arr = mypositionaddr.split(" ");
        String addrGu = "";
        for(String x : arr){
            if(x.charAt(x.length()-1)=='구'){
                addrGu = x;
            }
        }
        log.info("arrGu: {}",addrGu);
//        List<ShopVO> vos = shopService.findAllByShopAddrContaining(addrGu); //페이지처리없음
        List<ShopVO> vos = shopService.findAllByShopAddrContainingPageBlock(addrGu,cpage, pageBlock);
        model.addAttribute("vos", vos);


        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);


        // shop테이블에 들어있는 해당 구가 있는 모든음식점수는 몇개?
        long total_rows = shopService.getTotalRowswithContainingGu(addrGu);
        log.info("total_rows:" + total_rows);

        // 결과값 없을 경우, sorry페이지 이동 및 홈버튼 제공
        if(total_rows<=0){
            return "thymeleaf/shop/sorry";
        }

        // 페이징처리계산식
        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }

        // 페이지처리 화면단에 전달
        log.info("totalPageCount:" + totalPageCount);
        model.addAttribute("totalPageCount", totalPageCount);


        return "thymeleaf/shop/shopListMain";
    }

    // 카카오맵api 내주변맛집 조회 : 페이지네이션에서 조회
    @GetMapping("/mypositioncheckok")
    public String mypositioncheckokget(@RequestParam(name = "cpage", defaultValue = "1") int cpage,
                                    @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
                                    @RequestParam("mypositionaddr") String mypositionaddr, Model model) {

        log.info("/mypositioncheckok...");
        log.info("mypositionaddr:{}", mypositionaddr);

        String[] arr = mypositionaddr.split(" ");
        String addrGu = "";
        for(String x : arr){
            if(x.charAt(x.length()-1)=='구'){
                addrGu = x;
            }
        }
        log.info("arrGu: {}",addrGu);
//        List<ShopVO> vos = shopService.findAllByShopAddrContaining(addrGu); //페이지처리없음
        List<ShopVO> vos = shopService.findAllByShopAddrContainingPageBlock(addrGu,cpage, pageBlock);
        model.addAttribute("vos", vos);


        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);


        // shop테이블에 들어있는 해당 구가 있는 모든음식점수는 몇개?
        long total_rows = shopService.getTotalRowswithContainingGu(addrGu);
        log.info("total_rows:" + total_rows);

        // 결과값 없을 경우, sorry페이지 이동 및 홈버튼 제공
        if(total_rows<=0){
            return "thymeleaf/shop/sorry";
        }

        // 페이징처리계산식
        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }

        // 페이지처리 화면단에 전달
        log.info("totalPageCount:" + totalPageCount);
        model.addAttribute("totalPageCount", totalPageCount);


        return "thymeleaf/shop/shopListMain";
    }
    

    //정렬조회: 1별점 2최소금액 3리뷰수
    @GetMapping("/sortList.do")
    public String sortList(@RequestParam(name = "cpage", defaultValue = "1") int cpage,
                               @RequestParam(name = "pageBlock", defaultValue = "10") int pageBlock,
                               @RequestParam("sortNum") int sortNum,
                               @RequestParam(name = "userId", defaultValue = "") String userId,
                               Model model){
        log.info("shop list vos in controller sortList  sortNum:  "+sortNum);

        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);
        log.info("정렬 !!! userId : {},", userId);

        // 로그인 안한 경우
        List<ShopVO> vos = shopService.selectListSortPageBlock(sortNum,cpage, pageBlock);
//        List<ShopVO> vos = shopService.selectListByCategory(cateId); //페이지처리없음
        log.info("selectListBySortWithMinPricePageBlock list vos in controller :  "+vos);


        // shop테이블에 모든음식점수는 몇개?
        long total_rows = shopService.getTotalRows();
        log.info("total_rows:" + total_rows);


        // 로그인성공, userId 있을 경우 => 등록된 집주소 조회
        String userAddr = getUserAddrWithUserId.AddrGu(userId);
        log.info("유저 집주소 조회 :: userAddr:{}", userAddr);
        if(userId != null && !userId.isEmpty() && userAddr != null && !userAddr.isEmpty()){
            log.info("null도 아니고 그리고 엠티도 아니다 !!! 로그인성공이며 주소가 있다 ");


            // 로그인성공-userId집주소 주변 음식점 + 정렬 (집주소기준:~구 =>전체)  => 총 갯수 조회
            total_rows = shopService.getTotalRowswithContainingGu(userAddr);
            log.info("로그인성공 집주변 음식점+카테고리별 총갯수 total_rows:" + total_rows);
            log.info("로그인성공 카테고리별 로우갯수 !  total_rows:{},  ", total_rows);

            //로그인성공-userId집주소 주변 음식점 조회 + 정렬
            vos = shopService.findAllAddrPageWithSort(sortNum, userAddr, cpage, pageBlock);
            log.info("userId집주소 주변 음식점 조회+ 정렬 vos:{} ", vos);

            log.info("로그인 성공 ! 유저아이디 확인~~~~~~~~~~~~유저 집주소 조회 :: userId:{}", userId);
            model.addAttribute("userId", userId);
        }



        // 결과값 없을 경우, sorry페이지 이동 및 홈버튼 제공
        if(total_rows<=0){
            return "thymeleaf/shop/sorry";
        }

        // 페이징처리계산식
        long totalPageCount = 1;
        if (total_rows / pageBlock == 0) {
            totalPageCount = 1;
        } else if (total_rows % pageBlock == 0) {
            totalPageCount = total_rows / pageBlock;
        } else {
            totalPageCount = total_rows / pageBlock + 1;
        }

        // 페이지처리 화면단에 전달
        log.info("totalPageCount:" + totalPageCount);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("vos", vos);
        return "thymeleaf/shop/shopListMain";
    }

    //shopDatil/num=${vo.shopId})
    @GetMapping("/shopDetail")
    public String shopDetail(@RequestParam("num") int shopId, Model model){
        log.info("shopDetail called@#$@#$%#$@#!@#~~~~~~~~~~~~~~~~~~~~~~~~~");
        log.info("shopId: {}", shopId);
        model.addAttribute("shopId", shopId);

        ShopVO vo = shopService.findByShopId(shopId);
        model.addAttribute("vo", vo);

        List<Object[]> shopAndMenus = shopService.findShopWithMenu(shopId);
        List<MenuVO> dtos = new ArrayList<>();
        for (Object[] result : shopAndMenus) {
            // 배열의 요소에 접근하여 데이터 추출
            String menuName = (String) result[0];
            String menuPrice = (String) result[1];
            String menuDesc = (String) result[2];
            String menuPic = (String) result[3];
            int menuId =(int) result[4];

            // DTO에 데이터 저장
            MenuVO dto = new MenuVO(menuName, menuPrice, menuDesc, menuPic, menuId);
            dtos.add(dto);
        }
        model.addAttribute("dtos", dtos);

        return "thymeleaf/shop/shopDetail";
    }



}
