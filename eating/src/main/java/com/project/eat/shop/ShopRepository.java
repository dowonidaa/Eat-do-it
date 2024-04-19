package com.project.eat.shop;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShopRepository extends JpaRepository<ShopVO, Object> {

    public List<ShopVO> findAll();

    @Query(nativeQuery=true,
            value="select * from shop ORDER BY star_avg desc limit ?1, ?2")
    public List<ShopVO> selectAllPageBlock(Integer startRow,Integer pageBlock);

    public List<ShopVO> findAllByShopNameContaining(String searchWord);
    public List<ShopVO> findAllByCateId(int cate_id);

    public List<ShopVO> findAllByShopAddrContaining(String addrGu);

    @Query(nativeQuery=true,
            value="select * from shop "
                    + "		where shop_name like ?1 "
                    + "		ORDER BY star_avg desc limit ?2, ?3")
    public List<ShopVO> findAllByShopNamePage(String searchWord, int cpage, int pageBlock);

    @Query(nativeQuery=true,
            value="select count(*) total_rows from shop where shop_name like ?1 ")
    long countSearchListRows(String searchWord);

    @Query(nativeQuery=true,
            value="select count(*) total_rows from shop where cate_id = ?1 ")
    long countCategoryListRows(int cateId);


    @Query(nativeQuery=true,
            value="select * from shop "
                    + "		where cate_id = ?1 "
                    + "		ORDER BY star_avg desc limit ?2, ?3")
    List<ShopVO> findAllByCateIdListPageBlock(int cateId, int i, int pageBlock);

    @Query(nativeQuery=true,
            value="select count(*) total_rows from shop where shop_addr like ?1 ")
    long countAddrwithGu(String addrGu);

    @Query(nativeQuery=true,
            value="select * from shop "
                    + "		where shop_addr like ?1 "
                    + "		ORDER BY star_avg desc limit ?2, ?3")
    List<ShopVO> findAllByShopAddrContainingPageBlock(String addrGu, int i, int pageBlock);

    // 유저집주소 기준 주변음식점 + 검색키워드 총갯수
    @Query(nativeQuery=true,
            value="select count(*) total_rows from shop where shop_addr like ?1 and shop_name like ?2")
    long countAddrwithGuAndContaingSearchKey(String addrGu, String searchWord);

    // 유저집주소 기준 주변음식점 + 검색키워드
    @Query(nativeQuery=true,
            value="select * from shop "
                    + "		where shop_addr like ?1 "
                    + "		and shop_name like ?2 "
                    + "		ORDER BY star_avg desc limit ?3, ?4")
    List<ShopVO> findAllByShopNamePageMyAddr(String addrGu, String searchWord, int i, int pageBlock);

    // 유저집주소 기준 주변음식점 + 카테고리별 총갯수
    @Query(nativeQuery=true,
            value="select count(*) total_rows from shop where shop_addr like ?1 and cate_id = ?2 ")
    long countAddrwithGuAndContaingCateId(String addrGu, int cateId);

    // 유저집주소 기준 주변음식점 + 카테고리별
    @Query(nativeQuery=true,
            value="select * from shop "
                    + "		where shop_addr like ?1 "
                    + "		and cate_id = ?2 "
                    + "		ORDER BY star_avg desc limit ?3, ?4")
    List<ShopVO> findAllByCateIdListPageMyAddr(String s, int cateId, int i, int pageBlock);


    @Query(nativeQuery=true,
            value="select * from shop where replace(min_price,'_원','') order by min_price_int asc limit ?1, ?2")
    List<ShopVO> findAllBySortWithMinPrice(int i, int pageBlock);

    @Query(nativeQuery=true,
            value="select * from shop ORDER BY review_count desc limit ?1, ?2")
    List<ShopVO> findAllBySortWithReviewCnt(int i, int pageBlock);

    @Query(nativeQuery=true,
            value="select * from shop where shop_addr like ?1 and replace(min_price,'_원','') order by min_price_int asc limit ?2, ?3")
    List<ShopVO> findAllByShopAddrPageBlockSortMinPrice(String s, int i, int pageBlock);

    @Query(nativeQuery=true,
            value="select * from shop where shop_addr like ?1 ORDER BY review_count desc limit ?2, ?3")
    List<ShopVO> findAllByShopAddrPageBlockSortReviewCnt(String s, int i, int pageBlock);

    @Query(nativeQuery=true,
            value="SELECT m.menu_name, m.menu_price, m.menu_desc, m.menu_pic, m.menu_id FROM Shop s JOIN Menu m ON s.shop_name = m.shop_name WHERE s.shop_id = :shopId")
        List<Object[]> findShopWithMenu(@Param("shopId") int shopId);

    public ShopVO findByShopId(int shopId);

}
