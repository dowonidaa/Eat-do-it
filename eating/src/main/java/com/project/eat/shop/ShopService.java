package com.project.eat.shop;

import com.project.eat.admin.AdminVO_JPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopRepositoryEM shopRepositoryEM;


    public ShopVO approveShop(AdminVO_JPA adminVO) {
        ShopVO shopVO = new ShopVO();

        // AdminVO_JPA의 필요한 값들을 ShopVO에 설정
        shopVO.setShopId(adminVO.getShopId());
        shopVO.setShopName(adminVO.getShopName());
        shopVO.setStarAvg(adminVO.getStarAvg());
        shopVO.setReviewCount(adminVO.getReviewCount());
        shopVO.setDeliveryTime(adminVO.getDeliveryTime());
        shopVO.setDeliveryPrice(adminVO.getDeliveryPrice());
        shopVO.setRunTime(adminVO.getRunTime());
        shopVO.setShopTel(adminVO.getShopTel());
        shopVO.setShopAddr(adminVO.getShopAddr());
        shopVO.setMinPrice(adminVO.getMinPrice());
        shopVO.setTag(adminVO.getTag());
        shopVO.setCateId(adminVO.getCateId());
        shopVO.setShopThum(adminVO.getShopThum());
        shopVO.setMinPriceInt(adminVO.getMinPriceInt());

        return shopRepository.save(shopVO);
    }

    public List<ShopVO> selectAll(){
        return shopRepository.findAll();
    }

    public List<ShopVO> selectAllPageBlock(int cpage,int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;

        return shopRepository.selectAllPageBlock(startRow-1, pageBlock);

    }

    public List<ShopVO> searchList(String searchWord) {
        return shopRepository.findAllByShopNameContaining(searchWord);
    }


    public List<ShopVO> selectListByCategory(int cateId){
        return shopRepository.findAllByCateId(cateId);
    }

    public List<ShopVO> findAllByShopAddrContaining(String addrGu) {
        return shopRepository.findAllByShopAddrContaining(addrGu);
    }

    public long getTotalRows() {
        return shopRepository.count();
    }

    public List<ShopVO> searchListPageBlock(String searchWord, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return  shopRepository.findAllByShopNamePage("%"+searchWord+"%",startRow-1,pageBlock);
    }

    public long getSearchTotalRows(String searchWord) {
        return shopRepository.countSearchListRows("%"+searchWord+"%");
    }

    public long getTotalRowsByCategory(int cateId) {
        return shopRepository.countCategoryListRows(cateId);
    }

    public List<ShopVO> selectListByCategoryPageBlock(int cateId, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return shopRepository.findAllByCateIdListPageBlock(cateId,startRow-1,pageBlock);
    }

    public long getTotalRowswithContainingGu(String addrGu) {
        return shopRepository.countAddrwithGu("%"+addrGu+"%");
    }

    public List<ShopVO> findAllByShopAddrContainingPageBlock(String addrGu, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return shopRepository.findAllByShopAddrContainingPageBlock("%"+addrGu+"%",startRow-1,pageBlock);
    }

    public long countAddrwithGuAndContaingSearchKey(String addrGu,String searchWord){
        return  shopRepository.countAddrwithGuAndContaingSearchKey("%"+addrGu+"%", "%"+searchWord+"%");
    }

    // 유저집주소 기준 주변음식점 + 검색키워드
    public List<ShopVO> searchListPageBlockMyAddr(String addrGu, String searchWord, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return  shopRepository.findAllByShopNamePageMyAddr("%"+addrGu+"%", "%"+searchWord+"%",startRow-1,pageBlock);
    }

    public long countAddrwithGuAndContaingCateId(String userAddr, int cateId) {
        return shopRepository.countAddrwithGuAndContaingCateId("%"+userAddr+"%",cateId);
    }

    public List<ShopVO> cateIdListPageBlockMyAddr(String userAddr, int cateId, int cpage, int pageBlock) {
        int startRow = (cpage - 1) * pageBlock + 1;
        return  shopRepository.findAllByCateIdListPageMyAddr("%"+userAddr+"%", cateId,startRow-1,pageBlock);
    }


    public List<ShopVO> selectListSortPageBlock(int sortNum, int cpage, int pageBlock) {
        //1: 별점순 / 2:최소금액순 / 3:리뷰수순

        int startRow = (cpage - 1) * pageBlock + 1;
        if(sortNum==1){
            return shopRepository.selectAllPageBlock(startRow-1, pageBlock);
        } else if(sortNum==2){
            return shopRepository.findAllBySortWithMinPrice(startRow-1,pageBlock);
        } else if(sortNum==3){
            return shopRepository.findAllBySortWithReviewCnt(startRow-1,pageBlock);
        } else {
            return shopRepository.selectAllPageBlock(startRow-1, pageBlock); //디폴트 별점순
        }

    }

    public List<ShopVO> findAllAddrPageWithSort(int sortNum, String userAddr, int cpage, int pageBlock) {
        //1: 별점순 / 2:최소금액순 / 3:리뷰수순

        int startRow = (cpage - 1) * pageBlock + 1;
        if(sortNum==1){
            return shopRepository.findAllByShopAddrContainingPageBlock("%"+userAddr+"%",startRow-1,pageBlock);
        } else if(sortNum==2){
            return shopRepository.findAllByShopAddrPageBlockSortMinPrice("%"+userAddr+"%", startRow-1,pageBlock);
        } else if(sortNum==3){
            return shopRepository.findAllByShopAddrPageBlockSortReviewCnt("%"+userAddr+"%", startRow-1,pageBlock);
        } else {
            return shopRepository.findAllByShopAddrContainingPageBlock("%"+userAddr+"%",startRow-1,pageBlock); //디폴트 별점순
        }
    }

    public ShopVO findByShopId(Long shopId){
        return shopRepository.findByShopId(shopId);
    }


    public List<Object[]> findShopWithMenu(Long shopId) {
        return shopRepository.findShopWithMenu(shopId);
    }


    public ShopVO findShop(Long shopId) {
        return shopRepositoryEM.findShop(shopId);
    }

    public List<ShopVO> findAll() {
        return shopRepositoryEM.findAll();
    }





    public ShopVO findShopById(Long shopId) {
        return shopRepository.findByShopId(shopId);
    }

    //by shopId 가게명찾기
    public String getShopNameByShopId(Long shopId){
        return shopRepository.findShopNameByShopId(shopId);
    }


    public double getStaravgByShopId(Long shopId) {
        return shopRepository.findOneStarAvgByShopId(shopId);
    }

    @Transactional
    public void updateShopAverageRating(double starAvg, Long shopId) {
        shopRepository.updateStarAvgByShopId(starAvg, shopId);
    }
}
