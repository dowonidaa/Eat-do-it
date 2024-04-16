package com.project.eat.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReadyResponseVO {
    private String tid; // 결제 번호
    private String next_redirect_pc_url; // 결제 완료시 이동할 페이지
    private String created_at; // 주문한 사람 ID
}
