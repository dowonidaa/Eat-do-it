package com.project.eat.shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

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

    public ShopVO findByShopId(int shopId){
        return shopRepository.findByShopId(shopId);
    }

    public List<Object[]> findShopWithMenu(int shopId) {
        return shopRepository.findShopWithMenu(shopId);
    }
}
