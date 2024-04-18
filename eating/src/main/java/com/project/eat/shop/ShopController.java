package com.project.eat.shop;

import com.project.eat.cart.CartService;
import com.project.eat.cart.cartItem.CartItem;
import com.project.eat.item.Item;
import com.project.eat.item.itemOption.ItemOption;
import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final ShopService shopService;
    private final MemberService memberService;
    private final CartService cartService;

    @GetMapping("/shop/{shopId}")
    public String shop(@PathVariable("shopId") Long shopId, Model model, HttpSession session) {
        ShopVO findShop = shopService.findShop(shopId);
        List<Item> items = findShop.getItems();
        List<ItemOption> itemOptions = items.get(0).getItemOptions();


        model.addAttribute("shop", findShop);
        model.addAttribute("items", items);
        model.addAttribute("itemOptions", itemOptions);

        Object memberId = session.getAttribute("member_id");
        if(memberId != null) {
            MemberVO_JPA findMember = memberService.findOne(memberId.toString());
            if (findMember.getCart() == null) {
                cartService.createCart(memberId.toString());
            }
            if (findMember.getCart().getShop() == null) {
                ShopVO shop = shopService.findShop(shopId);
                model.addAttribute("cartShop", shop);
            }else {
                model.addAttribute("cartShop", findMember.getCart().getShop());

            }

                List<CartItem> cartItems = findMember.getCart().getCartItems();
                model.addAttribute("cartItems", cartItems);
                int totalPrice = findMember.getCart().getTotalPrice();
                model.addAttribute("totalPrice", totalPrice);


        }



//        return "/thymeleaf/shopPage";
        return "shop/shop_detail";
    }



}
