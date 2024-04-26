package com.project.eat.order;

import com.project.eat.cart.Cart;
import com.project.eat.cart.cartItem.CartItem;
import com.project.eat.cart.cartOption.CartItemOption;
import com.project.eat.item.Item;
import com.project.eat.member.MemberRepositoryEM;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.order.orderItem.OrderItem;
import com.project.eat.order.orderItemOption.OrderItemOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepositoryEM memberRepository;
    private final OrderDAO_JPA orderDAOJpa;

    @Transactional
    public Order createOrder(String memberId, OrderForm form) {
        MemberVO_JPA findMember = memberRepository.findOne(memberId);
        log.info("memberId = {}", findMember.getId());
        Cart cart = findMember.getCart();
        log.info("cartId = {}", cart.getId());
        log.info("address={}", form.getOrderAddress());
        log.info("orderType= {}", form.getOrderType());


        Order order = new Order();
        order.setDiscount(form.getDiscount());
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
        order.setOrderAddress(form.getOrderAddress());
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

    @Transactional
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

    public List<OrderDTO> findByOrderType(String memberId, OrderType orderType) {

        List<Order> findOrder = orderDAOJpa.findByMemberIdByOrderType(memberId, orderType);
        return getOrderDTOS(findOrder);
    }

    public List<Order> findByMemberIdByItemName(String memberId, String itemName) {
        return orderDAOJpa.findByItemNameContainingIgnoreCase(memberId, itemName);
    }

    public List<Order> findByOrdersBetweenDates(String memberId, LocalDate startDate, LocalDate endDate) {
        return orderDAOJpa.findByOrdersBetweenDates(memberId, startDate, endDate);
    }


    @Transactional
    public List<OrderDTO> findSearchForm(String memberId, SearchForm form) {

        List<Order> findOrders = orderRepository.searchListBetweenDates(memberId, form);
        log.info("findOrders.size()= {}", findOrders.size());
        List<OrderDTO> orders = new ArrayList<>();
        for (Order order : findOrders) {
            boolean reviewExists = order.getReview() != null;
            List<OrderItem> orderItems = order.getOrderItems();
            String matchedItemName = orderItems.stream()
                    .map(OrderItem::getItem)
                    .map(Item::getItemName)
                    .filter(name -> name.toLowerCase().contains(form.getSearchText().toLowerCase()))
                    .findFirst()
                    .orElse(orderItems.isEmpty() ? "" : orderItems.get(0).getItem().getItemName());
            String itemName = matchedItemName + (orderItems.size() - 1 != 0 ? " 외 " + (orderItems.size() - 1) + "개" : "");

            OrderDTO orderDTO = new OrderDTO(order.getId(), (order.getTotalPrice() + order.getOrderPrice() - order.getDiscount()), order.getOrderType(), order.getOrderStatus(), order.getPaymentMethod(), order.getShop().getShopId(), order.getShop().getShopThum(), order.getOrderDate(), order.getShop().getShopName(), itemName ,reviewExists);
            orders.add(orderDTO);
        }
        return orders;
    }

    public Long searchPageCount(String memberId, SearchForm form) {
        Long searchCount = orderRepository.searchPageCount(memberId, form);
        return (searchCount - 1) / form.getPageBlock() +1 ;
    }

    public List<OrderDTO> findAllPage(String memberId, SearchForm form) {

        List<Order> findOrder = orderRepository.findAllPage(memberId,form.getPageBlock());
        return getOrderDTOS(findOrder);
    }

    public Long pageCount(String memberId,SearchForm form) {

        int pageBLock = form.getPageBlock();
        Long totalCount = orderRepository.pageCount(memberId);

        return (totalCount - 1) / pageBLock + 1;
    }



    private List<OrderDTO> getOrderDTOS(List<Order> findOrder) {
        List<OrderDTO> orders = new ArrayList<>();
        for (Order order : findOrder) {
            boolean reviewExists = order.getReview() != null;
            log.info("reviewExists ={}", reviewExists);
            List<OrderItem> orderItems = order.getOrderItems();
            String itemName = orderItems.get(0).getItem().getItemName() + (orderItems.size() - 1 != 0 ? " 외 " + (orderItems.size() - 1) + "개" : "");
            OrderDTO orderDTO = new OrderDTO(order.getId(), (order.getTotalPrice() + order.getOrderPrice() - order.getDiscount()), order.getOrderType(), order.getOrderStatus(), order.getPaymentMethod(), order.getShop().getShopId(), order.getShop().getShopThum(), order.getOrderDate(), order.getShop().getShopName(), itemName, reviewExists );
            orders.add(orderDTO);
        }

        return  orders;
    }


}
