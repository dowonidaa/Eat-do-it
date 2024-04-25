package com.project.eat.order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString
public class SearchForm {

    private String selectedType;  //전체 배달 포장
    private String searchOption;  //전체기간 기간선택
    private String searchText;    // 검색어
    private LocalDate startDate;  // 시작날짜
    private LocalDate endDate;    //마지막날짜 -default 오늘날짜
    private int page;             // 페이지 기본 1페이지
    private int pageBlock;        // 페이징사이즈 5개
    private int totalCount;       // 총페이지수
    private boolean prev, next;

    public SearchForm() {
        this.page = 1;
        this.pageBlock=5;
        this.endDate = LocalDate.now();
    }
    public int getOffset(){
        return (page - 1) * pageBlock;
    }



}
