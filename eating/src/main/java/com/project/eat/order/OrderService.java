package com.project.eat.order;

import com.project.eat.cart.Cart;
import com.project.eat.cart.cartItem.CartItem;
import com.project.eat.cart.CartItemOption;
import com.project.eat.member.MemberRepositoryEM;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.order.orderItem.OrderItem;
import com.project.eat.order.orderItem.OrderItemRepository;
import com.project.eat.order.orderItemOption.OrderItemOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepositoryEM memberRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(String memberId, OrderForm form) {
        MemberVO_JPA findMember = memberRepository.findOne(memberId);
        log.info("memberId = {}", findMember.getId());
        Cart cart = findMember.getCart();
        log.info("cartId = {}", cart.getId());

        Order order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setShop(cart.getShop());
        order.setMember(findMember);
        order.setOrderType(form.getOrderType());
        order.setOrderPrice(cart.getShop().getDeliveryPrice());
        order.setOrderAddress(form.getOrderAddress());
        order.setOrderTel(form.getOrderTel());
        order.setPaymentMethod(form.getPaymentMethod());
        order.setDiscount(form.getDiscount());
        order.setMemberNotes(form.getMemberNotes());
        order.setOrderStatus(OrderStatus.READY);
        orderRepository.save(order);
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(cartItem.getItem());
            orderItem.setOrder(order);
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            order.addOrderItem(orderItem);
            for (CartItemOption cartItemOption : cartItem.getCartItemOptions()) {
                OrderItemOption orderItemOption = new OrderItemOption();
                orderItemOption.setOrderItem(orderItem);
                orderItemOption.setItemOption(cartItemOption.getItemOption());
                orderItem.addOrderItemOption(orderItemOption);
            }
        }
        return order;
    }

    public Order findOne(Long orderId) {
        return orderRepository.findOne(orderId);
    }

    public void deleteOne(Long orderId) {
        Order one = orderRepository.findOne(orderId);
        orderRepository.deleteOne(one);
    }

    @Transactional
    public void update(Order order) {
        Order findOrder = orderRepository.findOne(order.getId());
        findOrder.setTid(order.getTid());
        findOrder.setOrderStatus(order.getOrderStatus());
    }
}
