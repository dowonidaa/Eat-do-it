package com.project.eat.review;

import com.project.eat.shop.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;
@Slf4j
@Aspect
@Component
public class ReviewInterceptor {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ShopService shopService;
//("execution(public * test..*DAO*.delete(..))")
    @AfterReturning(
            pointcut = "execution(* com.project.eat.review.ReviewService.insertReview(..))",
            returning = "returnValue"
    )
    public void updateShopAverageRatingAfterReviewInsert(JoinPoint joinPoint, Object returnValue) {
        Object[] args = joinPoint.getArgs(); // 메서드의 매개변수를 가져옴

        Long shopId=null;
        double starAvg =0.0;
        int reviewStar =0;
        if (args.length > 0 && args[0] instanceof ReviewVO) {
            ReviewVO vo = (ReviewVO) args[0];
            log.info("ReviewInterceptor에서 shopId 추출... vo.getShopId(): {}", vo.getShopId());
            shopId = vo.getShopId();
            reviewStar = vo.getReviewStar();
            // 기존 shop starAvg값 가져오기
            starAvg = shopService.getStaravgByShopId(shopId);
            log.info("ReviewInterceptor에서 starAvg 추출 starAvg:{}",starAvg);
            log.info("ReviewInterceptor에서 starAvg 추출 reviewStar:{}",reviewStar);
            // 리뷰 삽입 후 별점 평균 업데이트 작업 수행
            // shopService.updateShopStarAvgRating(vo.getShopId());
        }

        if(shopId != null){
            if (Double.compare(starAvg, 0.0) == 0){
                starAvg = reviewStar;
                log.info("ReviewInterceptor에서 starAvg이 0일때 새리뷰별점으로 starAvg:{}",starAvg);
            } else{
                // (기존 별점+새리뷰)/2
                starAvg =(starAvg+reviewStar)/2;
                starAvg = Math.floor(starAvg * 10) / 10;

                log.info("계산된 starAvg:{}",starAvg);
            }

            // 리뷰 삽입 후 별점 평균 업데이트 작업 수행
            shopService.updateShopAverageRating(starAvg,shopId);
        }
    }



}
