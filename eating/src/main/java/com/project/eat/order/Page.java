package com.project.eat.order;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Page {

    private Long startPage;
    private Long endPage;
    private boolean prev, next;

    public Page(Long startPage, Long endPage, Long totalPage) {
        this.prev = startPage != 1;
        this.next = !endPage.equals(totalPage);
        this.startPage = startPage;
        this.endPage = endPage;

    }

}
