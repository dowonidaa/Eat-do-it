package com.project.eat.review;

import com.project.eat.shop.GetUserAddrWithUserId;
import com.project.eat.shop.ShopService;
import com.project.eat.shop.ShopVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Base64;


import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private GetUserAddrWithUserId getUserAddrWithUserId;

    @Autowired
    private UploadService uploadService;

    @Autowired
    private BadWordService badWordService;

    @Resource(name="WordAnalysisService")
    private IWordAnalysisService wordAnalysisService;

    @Autowired
    private ImageCall imageCall; // imageCall 주입

    // 리뷰 폼 페이지
    // POST 방식으로 리뷰 폼 페이지 요청
    @PostMapping ("/review_formPage")
    public String reviewFormPage(@RequestParam(name = "shopId") Long shopId,
                                 @RequestParam(name = "itemsName") String itemsName,
                                 Model model) {
        // 필요한 데이터를 모델에 추가
        model.addAttribute("shopId", shopId);
        log.info("shopId:{}", shopId);
        model.addAttribute("itemsName", itemsName);
        log.info("itemsName:{}", itemsName);
        return "thymeleaf/review/reviewFormPage";
    }

//    @GetMapping("/review_formPage")
//    public String review_formPage(){
//
//        return "thymeleaf/review/reviewFormPage";
//    }


    // 리뷰폼ok 처리 :form input post방식
    @PostMapping("/review_formdata")
    public String review_formdata(@RequestParam(name = "reviewStar", defaultValue = "1") int reviewStar,
                                    @RequestParam(name = "reviewComent") String reviewComent,
                                    @RequestParam(name = "file", required = false) MultipartFile file,
                                    @RequestParam(name = "shopId", defaultValue = "1") Long shopId,
                                    @RequestParam(name = "cpage", defaultValue = "1") int cpage,
                                    @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
                                    @RequestParam(name = "userId", defaultValue = "") int userId,
                                    Model model) throws Exception {
        log.info("/review_formdata...");
        log.info("reviewStar:{}", reviewStar);
        log.info("reviewComent:{}", reviewComent);
        log.info("shopId : {}, userId : {}", shopId, userId);

        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);

        String reviewPic="";
        //이미지처리
        if (file != null && !file.isEmpty()) {
            // 파일이 선택된 경우의 처리
            try {
                // 파일을 업로드하고 업로드된 파일의 URL을 반환
                String fileUrl = uploadService.uploadFile(file);
                // 파일의 URL을 반환하여 클라이언트에게 전송하거나 다른 작업을 수행할 수 있습니다.
                reviewPic= fileUrl;
            } catch (IOException e) {
                e.printStackTrace();
                reviewPic= "";
            }
        } else {
            // 파일이 선택되지 않은 경우의 처리
            reviewPic= "";
        }

        log.info("확인 !!! reviewPic:{}", reviewPic);

        ReviewVO vo = new ReviewVO();

        vo.setReviewStar(reviewStar);
//        vo.setShopId(shopId);
        // ShopVO를 설정
        ShopVO shop = shopService.findShopById(shopId); // shopId에 해당하는 가게 정보를 가져옴
        vo.setShop(shop);

        vo.setUserId(userId);
        vo.setReviewPic(reviewPic);

        //리뷰필터
//        Map<String, Integer> rMap = wordAnalysisService.doWordAnalysis(reviewComent);
//        log.info(" 컨트롤러에서... map 결과값...rMap:{}",rMap);

        List<String> rMap2 = wordAnalysisService.doWordNouns(reviewComent);
        log.info(" 컨트롤러에서... List 결과값...rMap2:{}",rMap2);

        int pass1 = wordAnalysisService.checkdoBadWord(reviewComent);
        log.info("1이면 정상. 0이면 비정상... pass1:{}",pass1);
        if(pass1==0){
            reviewComent = "해당 리뷰는 관리자에 의해 보이지 않습니다";
            vo.setReviewComent(reviewComent);
        } else {
            vo.setReviewComent(reviewComent);
        }

        reviewService.insertReview(vo);


        log.info("insert... /review_formdata...");
//        List<ReviewVO> listReviewVos = reviewService.selectListByShopId(shopId);
        List<ReviewVO> listReviewVos = reviewService.selectListByShopId(shopId,cpage, pageBlock);


        log.info("review_formdata...... /review_formdata...");


        // 총갯수 by ShopId
        // 키워드 검색한 shop테이블에 들어있는 getSearchTotalRows 몇개?
        long total_rows = reviewService.getTotalRowsByShopId(shopId);
        log.info("total_rows:" + total_rows);
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

        // by shopId 로 가게명 기재
        String shopName = shopService.getShopNameByShopId(shopId);
        model.addAttribute("shopName", shopName);

        model.addAttribute("listReviewVos", listReviewVos);

        return "thymeleaf/review/reviewListByShopId";
    }

    // by shopId : 페이지
    @GetMapping("/review_formdata_get")
    public String review_formdata_get (
                                  @RequestParam(name = "shopId", defaultValue = "1") Long shopId,
                                  @RequestParam(name = "cpage", defaultValue = "1") int cpage,
                                  @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
                                  Model model) throws IOException {
        log.info("가게 상세에서 a 링크로 by shopId 로 리뷰리스트 구성 조회 확인 !!! ");
        log.info("/review_formdata_get...");

        log.info("cpage : {}, pageBlock : {}", cpage, pageBlock);


        List<ReviewVO> listReviewVos = reviewService.selectListByShopId(shopId,cpage, pageBlock);
        log.info("review_formdata_get...... /review_formdata_get...");



        model.addAttribute("listReviewVos", listReviewVos);


        // 총갯수 by ShopId
        // 키워드 검색한 shop테이블에 들어있는 getSearchTotalRows 몇개?
        long total_rows = reviewService.getTotalRowsByShopId(shopId);
        log.info("total_rows:" + total_rows);
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

        // by shopId 로 가게명 기재
        String shopName = shopService.getShopNameByShopId(shopId);
        model.addAttribute("shopName", shopName);

        // 이미지 데이터 가져오기
        for (ReviewVO review : listReviewVos) {
            log.info("체크2...");
            byte[] imageData = imageCall.getImage(review.getReviewPic()).getBody();
            if (imageData != null) {
                // 이미지를 Base64로 변환하여 모델에 추가
                String base64Image = Base64.getEncoder().encodeToString(imageData);
//                log.info("이미지 데이터 확인: {}", base64Image);
                model.addAttribute(review.getReviewPic(), base64Image);
            }
        }

        String absolutePath = "C:/upload/test.png"; // 절대 경로 설정
        model.addAttribute("imagePath", absolutePath);


        return "thymeleaf/review/reviewListByShopId";
    }


    // 리뷰 마이 페이지
    @GetMapping("/review_Mypage")
    public String review_MypageList(
//            HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(name = "cpage", defaultValue = "1") int cpage,
                                @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
                                @RequestParam(name = "memberId", defaultValue = "") String memberId,
                                Model model)  throws IOException  {

        log.info("review_Mypage-- memberId:{}", memberId);
        // by memberId 로 num 타입:int 획득
        int  userId = reviewService.findUserIdByMemberId(memberId);
        log.info("확인 !!! userId:{}", userId);

        // 내가 작성한 리뷰리스트
        List<ReviewVO> myReviewList = reviewService.selectListByUserId(userId,cpage, pageBlock);


        log.info("review_Mypage...... /review_Mypage...");
        model.addAttribute("myReviewList", myReviewList);


        // by memberId : 총갯수
        long total_rows = reviewService.getTotalRowsByUserId(userId);
        log.info("total_rows:" + total_rows);
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

        return "thymeleaf/review/reviewMypageList";
    }

    // selectOne
    @GetMapping("/reviewMypageSelectOne")
    public String reviewMypageSelectOne(
            @RequestParam(name = "reviewId") int reviewId,
            @RequestParam(name = "shopName") String shopName,
            @RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "userId") int userId,
            Model model){
        log.info("reviewMypageSelectOne-- reviewId:{}", reviewId);
        log.info("reviewMypageSelectOne-- shopName:{}", shopName);
        log.info("reviewMypageSelectOne-- memberId:{}", memberId);
        log.info("reviewMypageSelectOne-- userId:{}", userId);

        ReviewVO vo = reviewService.findOneByReviewId(reviewId);
        model.addAttribute("vo", vo);

        return "thymeleaf/review/reviewMypageOne";
    }

    // 수정 페이지 조회
    @GetMapping("/reviewMypageUpdateOne")
    public String reviewMypageUpdateOne(
            @RequestParam(name = "reviewId") int reviewId,
            @RequestParam(name = "shopName") String shopName,
            @RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "userId") int userId,
            Model model){
        log.info("    reviewMypageUpdateOne-- reviewId:{}", reviewId);
        log.info("    reviewMypageUpdateOne- shopName:{}", shopName);
        log.info("    reviewMypageUpdateOne-- memberId:{}", memberId);
        log.info("    reviewMypageUpdateOne-- userId:{}", userId);

        // read one
        ReviewVO vo = reviewService.findOneByReviewId(reviewId);
        model.addAttribute("vo", vo);

        return "thymeleaf/review/reviewUpdateFormPage";
    }

    //수정 post review_formdata_update
    @PostMapping("/review_formdata_update")
    public String review_formdata_update(@RequestParam(name = "reviewStar", defaultValue = "1") int reviewStar,
                                  @RequestParam(name = "reviewComent") String reviewComent,
                                  @RequestParam(name = "file", required = false) MultipartFile file,
                                  @RequestParam(name = "shopId", defaultValue = "1") Long shopId,
                                  @RequestParam(name = "userId", defaultValue = "") int userId,
                                  @RequestParam(name = "reviewId", defaultValue = "") int reviewId,
                                  @RequestParam(name = "shopName", defaultValue = "") String shopName,
                                  @RequestParam(name = "memberId", defaultValue = "") String memberId,
                                  Model model) throws IOException {
        log.info("수정!!!! /review_formdata_update...");
        log.info("reviewStar:{}", reviewStar);
        log.info("reviewComent:{}", reviewComent);
        log.info("shopId : {}, userId : {}", shopId, userId);


        String reviewPic = "";
        //이미지처리
        if (file != null && !file.isEmpty()) {
            // 파일이 선택된 경우의 처리
            try {
                // 파일을 업로드하고 업로드된 파일의 URL을 반환
                String fileUrl = uploadService.uploadFile(file);
                // 파일의 URL을 반환하여 클라이언트에게 전송하거나 다른 작업을 수행할 수 있습니다.
                reviewPic = fileUrl;
            } catch (IOException e) {
                e.printStackTrace();
                reviewPic = "";
            }
        } else {
            // 파일이 선택되지 않은 경우의 처리
            reviewPic = "";
        }

        log.info("확인 !!! reviewPic:{}", reviewPic);

        ReviewVO inservo = reviewService.findOneByReviewId(reviewId);
        int inservoReviewId = inservo.getReviewId();
        log.info("찾은 객체의 id값 확인 ~~~~ inservoReviewId:{}", inservoReviewId);

        inservo.setReviewComent(reviewComent);
        inservo.setReviewStar(reviewStar);
        inservo.setReviewPic(reviewPic);

        reviewService.insertReview(inservo);
        log.info("수정 저장함!  Db확인 id값 같은거에 수정값 반영됏는지확인");



        log.info("reviewMypageSelectOne-- reviewId:{}", reviewId);
        log.info("reviewMypageSelectOne-- shopName:{}", shopName);
        log.info("reviewMypageSelectOne-- userId:{}", userId);
        log.info("reviewMypageSelectOne-- memberId:{}", memberId);

        ReviewVO vo = reviewService.findOneByReviewId(reviewId);
        model.addAttribute("vo", vo);

        return "thymeleaf/review/reviewMypageOne";
    }


    // 삭제
    @GetMapping("/reviewMypagedeleteOne")
    public String reviewMypagedeleteOne(
            @RequestParam(name = "reviewId") int reviewId,
            @RequestParam(name = "memberId") String memberId,
            @RequestParam(name = "userId") int userId,
            @RequestParam(name = "cpage", defaultValue = "1") int cpage,
            @RequestParam(name = "pageBlock", defaultValue = "2") int pageBlock,
            Model model){
        log.info("reviewMypagedeleteOne-- reviewId:{}", reviewId);
        log.info("reviewMypagedeleteOne-- memberId:{}", memberId);
        log.info("reviewMypagedeleteOne-- userId:{}", userId);

        // reviewId 로 삭제
        reviewService.deleteByReviewId(reviewId);

        log.info("review_Mypage-- memberId:{}", memberId);
        log.info("확인 !!! userId:{}", userId);

        // 내가 작성한 리뷰리스트
        List<ReviewVO> myReviewList = reviewService.selectListByUserId(userId,cpage, pageBlock);

        log.info("reviewMypagedeleteOne...... /reviewMypagedeleteOne...");
        model.addAttribute("myReviewList", myReviewList);


        // by memberId : 총갯수
        long total_rows = reviewService.getTotalRowsByUserId(userId);
        log.info("total_rows:" + total_rows);
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

        return "thymeleaf/review/reviewMypageList";
    }





}
