package com.project.eat.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;


    public void insertReview(ReviewVO vo){
        reviewRepository.save(vo);
    }

    // by shopId 리뷰리스트 전체조회
    public List<ReviewVO> selectListByShopId(Long shopId, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return reviewRepository.findReviewListByShopId(shopId,startRow-1, pageBlock);
    }

    // by shopId 리뷰리스트 총갯수
    public long getTotalRowsByShopId(Long shopId){
        return reviewRepository.countByShopId(shopId);
    }

    // by userId 마이페이지에서 나의 리뷰리스트 총갯수
    public long getTotalRowsByUserId(int userId){
        return reviewRepository.countByUserId(userId);
    }


    public int findUserIdByMemberId(String memberId) {
        return reviewRepository.findUserIdByMemberId(memberId);
    }

    public List<ReviewVO> selectListByUserId(int userId, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return reviewRepository.findAllByUserId(userId, startRow-1, pageBlock);
    }

    public ReviewVO findOneByReviewId(int reviewId) {
        return reviewRepository.findOneByReviewId(reviewId);
    }

    public void deleteByReviewId(int reviewId){
        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    public void update(ReviewVO updateReview) {
        ReviewVO findReview = reviewRepository.findOneByReviewId(updateReview.getReviewId());
        if(!updateReview.getReviewPic().equals("")) {
            findReview.setReviewPic(updateReview.getReviewPic());
        }
        findReview.setReviewComent(updateReview.getReviewComent());
        findReview.setReviewStar(updateReview.getReviewStar());
        findReview.setShopName(updateReview.getShopName());

    }
}
