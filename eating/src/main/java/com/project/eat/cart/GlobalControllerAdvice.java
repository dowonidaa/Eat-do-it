package com.project.eat.cart;

import com.project.eat.member.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CartService cartService;
    private final MemberService memberService;

    @ModelAttribute("cartCount")
    public int cartCount(HttpSession session) {
        Object memberId = session.getAttribute("member_id");
        if (memberId != null) {
            if(memberService.findOne(memberId.toString()).getCart()!=null) {
            return cartService.countCartItems(memberId.toString());
            }
        }
        return 0;

    }

    @ModelAttribute("shopId")
    public Long shopId(HttpSession session) {
        String memberId =(String) session.getAttribute("member_id");
        if (memberId != null) {
            if(memberService.findOne(memberId).getCart()!=null) {
                return cartService.findShopId(memberId);
            }
        }
        return null;
    }

}
