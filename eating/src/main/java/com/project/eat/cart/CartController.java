package com.project.eat.cart;

import com.project.eat.cart.cartItem.CartItem;
import com.project.eat.cart.cartItem.CartItemService;
import com.project.eat.item.Item;
import com.project.eat.item.itemOption.ItemOption;
import com.project.eat.item.ItemService;
import com.project.eat.item.itemOption.ItemOptionService;
import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final MemberService memberService;
    private final ItemService itemService;
    private final CartItemService cartItemService;
    private final ItemOptionService itemOptionService;


    @ResponseBody
    @PostMapping("/{shopId}/add")
    public ResponseEntity<Map<String, Object>> addCart(@PathVariable("shopId") Long shopId, Long itemId, Long itemOptionId, @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        String memberId = (String)session.getAttribute("member_id");
        Map<String, Object> response = new HashMap<>();
        response.put("shopId", shopId);
        if (itemOptionId == null) {
            return ResponseEntity.ok(response);
        }
        MemberVO_JPA findMember = memberService.findOne(memberId);
        Cart memberCart = findMember.getCart();

        if (memberCart == null) {
            cartService.createCart(memberId);
            memberCart = findMember.getCart();
        }
        if (memberCart.getShop() != null) {
            if (memberCart.getShop().getId() != shopId) {
                cartService.deleteAndCreateCart(memberId, shopId);
                memberCart = findMember.getCart();
            }
        } else {
            cartService.setShopCart(memberId, shopId);
        }

        ItemOption itemOption = itemOptionService.findOne(itemOptionId);
        Item item = itemService.findOne(itemId);
        int cartPrice = item.getItemPrice() + itemOption.getPrice();

        cartItemService.saveCartItem(memberId, itemId, itemOptionId, quantity, cartPrice, memberCart);

        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @DeleteMapping("/{cartItemId}/delete")
    public ResponseEntity<CartItem> deleteCartItem(@PathVariable("cartItemId") Long cartItemId, HttpSession session) {
        String memberId = (String)session.getAttribute("member_id");
        MemberVO_JPA findMember = memberService.findOne(memberId);

        if (findMember.getCart().getCartItems().size() <= 1) {
            cartService.delete(findMember.getCart());
            cartService.createCart(memberId);
        }else {
            cartItemService.findAndDelete(cartItemId);
        }

        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PostMapping("/{cartItemId}/increment")
    public ResponseEntity<Map<String, Object>> increment(@PathVariable("cartItemId") Long cartItemId) {
        CartItem cartItem = cartItemService.increaseQuantity(cartItemId);
        int newPrice = cartItem.getPrice();
        Map<String, Object> response = new HashMap<>();
        response.put("newQuantity", cartItem.getQuantity());
        response.put("newPrice", newPrice);
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @PostMapping("/{cartItemId}/decrement")
    public ResponseEntity<Map<String, Object>> decrement(@PathVariable("cartItemId") Long cartItemId) {
        CartItem cartItem = cartItemService.decreaseQuantity(cartItemId);
        int newPrice = cartItem.getPrice();
        Map<String, Object> response = new HashMap<>();
        response.put("newQuantity", cartItem.getQuantity());
        response.put("newPrice", newPrice);
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @GetMapping("/total-price")
    public ResponseEntity<Map<String, Integer>> totalPrice(HttpSession session) {
        String memberId = session.getAttribute("member_id").toString();
        int totalPrice = cartService.getTotalPrice(memberId);
        Map<String, Integer> response = new HashMap<>();
        response.put("totalPrice", totalPrice);
        return ResponseEntity.ok(response);
    }

    @ResponseBody
    @PostMapping("/remove")
    public ResponseEntity<Map<String, Object>> cartRemove(HttpSession session, Long shopId) {
        log.info("{}", shopId);
        String memberId = session.getAttribute("member_id").toString();
        cartService.deleteAndCreateCart(memberId, shopId);
        log.info("remove");
        return ResponseEntity.ok().build();
    }


}
