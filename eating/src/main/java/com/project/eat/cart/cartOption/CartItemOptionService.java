package com.project.eat.cart.cartOption;

import com.project.eat.cart.CartItemOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartItemOptionService {

    private final CartItemOptionRepository cartItemOptionRepository;

    public CartItemOption findOne(Long cartItemOptionId) {
        return cartItemOptionRepository.findOne(cartItemOptionId);
    }

    @Transactional
    public void save(CartItemOption cartItemOption) {
        cartItemOptionRepository.save(cartItemOption);

    }
}
