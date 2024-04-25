package com.project.eat.cart;

import com.project.eat.cart.cartItem.CartItem;
import com.project.eat.cart.cartItem.CartItemService;
import com.project.eat.item.Item;
import com.project.eat.item.ItemService;
import com.project.eat.item.itemOption.ItemOption;
import com.project.eat.item.itemOption.ItemOptionService;
import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.shop.ShopService;
import com.project.eat.shop.ShopVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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
    private final ShopService shopService;


    @PostMapping("/{shopId}/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addCart(@PathVariable("shopId") Long shopId, Long itemId, Long itemOptionId, @RequestParam(defaultValue = "1") int quantity, @RequestParam(value = "extraItemId", required = false) List<Long> extraItemId, HttpSession session) {
        String memberId = (String) session.getAttribute("member_id");
        Map<String, Object> response = new HashMap<>();
        response.put("shopId", shopId);
        log.info("extraItemId = {}",extraItemId);
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
            if (memberCart.getShop().getShopId() != shopId) {
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
        if (!extraItemId.isEmpty()) {
            for (Long extraIt : extraItemId) {
                if(extraIt==null){
                    continue;
                }

            Item extraItem = itemService.findOne(extraIt);
            ItemOption extraItemOption = extraItem.getItemOptions().get(3);
            int extraPrice = extraItem.getItemPrice() + extraItemOption.getPrice();
            cartItemService.saveCartItem(memberId, extraIt, extraItemOption.getId(), quantity, extraPrice, memberCart);
            }
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public String deleteCartItem(Long cartItemId, HttpSession session) {
        String memberId = (String) session.getAttribute("member_id");
        MemberVO_JPA findMember = memberService.findOne(memberId);
        Long shopId = findMember.getCart().getShop().getShopId();
        log.info("cartItemId={}", cartItemId);

        if (findMember.getCart().getCartItems().size() <= 1) {
            cartService.delete(findMember.getCart());
            cartService.createCart(memberId);

        } else {
            cartItemService.findAndDelete(cartItemId, memberId);

            findMember.getCart().findAndDeleteCartItem(cartItemId);
            cartService.getTotalPrice(memberId);


        }

        return "redirect:/shopDetail?num=" + shopId;
    }

    @PostMapping("/{cartItemId}/increment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> increment(@PathVariable("cartItemId") Long cartItemId) {
        CartItem cartItem = cartItemService.increaseQuantity(cartItemId);
        int newPrice = cartItem.getPrice();
        Map<String, Object> response = new HashMap<>();
        response.put("newQuantity", cartItem.getQuantity());
        response.put("newPrice", newPrice);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{cartItemId}/decrement")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> decrement(@PathVariable("cartItemId") Long cartItemId) {
        CartItem cartItem = cartItemService.decreaseQuantity(cartItemId);
        int newPrice = cartItem.getPrice();
        Map<String, Object> response = new HashMap<>();
        response.put("newQuantity", cartItem.getQuantity());
        response.put("newPrice", newPrice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/total-price")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> totalPrice(HttpSession session, Long shopId) {
        String memberId = (String) session.getAttribute("member_id");
        log.info("shopId = {}", shopId);
        ShopVO shop = memberService.findOne(memberId).getCart().getShop();
        int minPriceInt = 0;
        if (shop != null) {
            minPriceInt = shop.getMinPriceInt();
        } else {
            minPriceInt = shopService.findShop(shopId).getMinPriceInt();

        }
        int totalPrice = cartService.getTotalPrice(memberId);
        Map<String, Integer> response = new HashMap<>();
        response.put("totalPrice", totalPrice);
        response.put("minPrice", minPriceInt);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove")
    public String cartRemove(HttpSession session, Long shopId) {
        log.info("{}", shopId);
        String memberId = (String) session.getAttribute("member_id");
        cartService.deleteCart(memberId);
        log.info("remove");
        return "redirect:/shopDetail?num=" + shopId;
    }

    @PostMapping("/{itemId}")
    public String getItemDetails(@PathVariable("itemId") Long itemId, Model model) {
        Item findItem = itemService.findOne(itemId);
        ShopVO itemShop = findItem.getShop();
        List<Item> items = itemShop.getItems().stream().filter(item -> !item.getId().equals(itemId)).toList();
        List<ItemOption> itemOptions = findItem.getItemOptions().stream().sorted(Comparator.comparing(ItemOption::getPrice)).toList();
        model.addAttribute("item", findItem);
        model.addAttribute("items", items);
        model.addAttribute("itemOptions", itemOptions);
        model.addAttribute("shop", itemShop);
        return "shop/itemAddForm";
    }


}
