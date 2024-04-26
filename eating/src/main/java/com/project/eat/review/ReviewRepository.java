package com.project.eat.review;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewVO, Object> {


    @Query(nativeQuery=true,
            value="select m.name, r.shop_name , r.review_star, r.review_id, r.shop_id, r.created_at, r.review_coment, r.review_pic, r.user_id, r.order_id  "
                    + "		from review r join member m on m.num=r.user_id where r.shop_id = ?1 order by r.created_at desc "
                    + "		limit ?2, ?3")
    List<ReviewVO> findReviewListByShopId(Long shopId, int i, int pageBlock);

    @Query(nativeQuery=true,
            value="select count(*) total_rows from review where shop_id = ?1 ")
    long countByShopId(Long shopId);

    @Query(nativeQuery=true,
            value="select count(*) total_rows from review where user_id = ?1 ")
    long countByUserId(int userId);

    @Query(nativeQuery=true,
            value="select distinct r.user_id from review r join member m on m.num = r.user_id where m.member_id = ?1  ")
    int findUserIdByMemberId(String memberId);

    @Query(nativeQuery=true,
            value="select s.shop_name, r.name, r.review_star, r.review_id, r.shop_id, r.created_at, r.review_coment, r.review_pic, r.user_id, r.order_id  "
                    + "		from review r join shop s on s.shop_id=r.shop_id where r.user_id = ?1 order by r.created_at desc "
                    + "		limit ?2, ?3")
    List<ReviewVO> findAllByUserId(int userId, int i, int pageBlock);

    ReviewVO findOneByReviewId(int reviewId);

}
