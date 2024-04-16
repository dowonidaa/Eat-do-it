package com.project.eat.cart.cartItem;

import com.project.eat.cart.Cart;
import com.project.eat.cart.CartItemOption;
import com.project.eat.cart.cartOption.CartItemOptionRepository;
import com.project.eat.item.Item;
import com.project.eat.item.ItemOption;
import com.project.eat.item.ItemRepository;
import com.project.eat.item.itemOption.ItemOptionRepository;
import com.project.eat.member.MemberRepositoryEM;
import com.project.eat.member.MemberVO_JPA;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final MemberRepositoryEM memberRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final CartItemOptionRepository cartItemOptionRepository;

    @Transactional
    public void save(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void saveCartItem(String memberId, Long itemId, Long itemOptionId, int quantity, int price, Cart memberCart) {
        // 장바구니 음식 같은 음식이면 수량만 증가
        // 옵션 다르면 새롭게 추가
        MemberVO_JPA findMember = memberRepository.findOne(memberId);
        List<CartItem> cartItems = findMember.getCart().getCartItems();

        for (CartItem cartItem : cartItems) {
            if (cartItem.getItem().getId().equals(itemId)) {
                log.info("itemId= {}", cartItem.getItem().getId());
                List<CartItemOption> cartItemOptions = cartItem.getCartItemOptions();
                for (CartItemOption cartItemOption : cartItemOptions) {
                    if (cartItemOption.getItemOption().getId().equals(itemOptionId)) {
                        cartItem.setQuantity(cartItem.getQuantity() + quantity);
                        cartItem.cartPrice();
                        findMember.getCart().totalPrice();
                        return;
                    }
                }
            }
        }
        Item selectItem = itemRepository.findOne(itemId);
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(quantity);
        cartItem.setCart(memberCart);
        cartItem.setPrice(price * quantity);
        cartItem.setItem(selectItem);
        cartItemRepository.save(cartItem);
        CartItemOption cartItemOption = getCartItemOption(itemOptionId, cartItem);
        cartItemOptionRepository.save(cartItemOption);
        cartItem.addCartItemOption(cartItemOption);
        findMember.getCart().addCartItem(cartItem);
        findMember.getCart().totalPrice();
        log.info("cartItemService end");
    }




    public CartItem findOne(Long cartItemId) {
        return cartItemRepository.findOne(cartItemId);
    }

    @Transactional
    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void findAndDelete(Long cartItemId) {
        CartItem one = cartItemRepository.findOne(cartItemId);
        cartItemRepository.delete(one);
    }


    @Transactional
    public CartItem increaseQuantity(Long cartItemId) {
        CartItem findCartItem = cartItemRepository.findOne(cartItemId);
        findCartItem.setQuantity(findCartItem.getQuantity()<99 ? findCartItem.getQuantity() + 1 : findCartItem.getQuantity());
        findCartItem.cartPrice();
        return findCartItem;
    }

    @Transactional
    public CartItem decreaseQuantity(Long cartItemId) {
        CartItem findCartItem = cartItemRepository.findOne(cartItemId);
        findCartItem.setQuantity(findCartItem.getQuantity()>1 ? findCartItem.getQuantity() - 1 : findCartItem.getQuantity());
        findCartItem.cartPrice();
        return findCartItem;
    }





    private CartItemOption getCartItemOption(Long itemOptionId, CartItem cartItem) {
        ItemOption selectItemOption = itemOptionRepository.findOne(itemOptionId);
        CartItemOption cartItemOption = new CartItemOption();
        cartItemOption.setItemOption(selectItemOption);
        cartItemOption.setCartItem(cartItem);
        return cartItemOption;
    }


}
