package com.project.eat.order;

import com.project.eat.cart.Cart;
import com.project.eat.cart.CartService;
import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.order.orderItem.OrderItem;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final MemberService memberService;
    private final KakaoPayServiceImpl kakaopayService;

    @GetMapping("/order")
    public String orderPage(Model model, HttpSession session, OrderForm orderForm) {
        String memberId = session.getAttribute("member_id").toString();
        MemberVO_JPA findMember = memberService.findOne(memberId);
        if (findMember.getCart().getTotalPrice() < findMember.getCart().getShop().getMinPriceInt()) {
            return "redirect:/shop/" + findMember.getCart().getShop().getShopId();
        }
        log.info(findMember.getAddress().getAddress());
        Cart cart = findMember.getCart();
        model.addAttribute("cart", cart);
        model.addAttribute("member", findMember);
        model.addAttribute("orderForm", orderForm);

        if (orderForm.getOrderType() == OrderType.DELIVERY) {
        return "pay/orderPage";

        }else {
            return "pay/takeoutPage";
        }
    }

    @PostMapping("/order")
    public String order(HttpSession session, OrderForm form) {
        String memberId = session.getAttribute("member_id").toString();

        // 주문 완료 오더테이블 저장하면 장바구니 삭제
        cartService.deleteCart(memberId);

        return "redirect:/orders";
    }


    @GetMapping("/orders")
    public String orderList(HttpSession session, Model model) {

        String memberId = session.getAttribute("member_id").toString();
        MemberVO_JPA findMember = memberService.findOne(memberId);
        List<Order> orders = findMember.getOrders();
        model.addAttribute("orders", orders);
        return "/order/orderList";
    }

    @GetMapping("/orders/{orderId}")
    public String orderDetail(@PathVariable("orderId")Long orderId, HttpSession session, Model model) {
        Order findOrder = orderService.findOne(orderId);
        model.addAttribute("order", findOrder);
        return "/order/orderDetail";
    }

    @PostMapping("/orders/{orderId}/delete")
    public String orderDelete(@PathVariable("orderId") Long orderId) {
        orderService.deleteOne(orderId);
        return "redirect:/orders";
    }

    @GetMapping("/order/kakao")
    @ResponseBody
    public  ReadyResponseVO payReady(OrderForm form, Model model, HttpSession session) {

        String memberId = session.getAttribute("member_id").toString();
        Order order = orderService.createOrder(memberId, form);
        List<OrderItem> orderItems = order.getOrderItems();
        String[] cartNames = new String[orderItems.size()];
        for (OrderItem orderItem : orderItems) {
            for (int i = 0; i < orderItems.size(); i++) {
                cartNames[i] = orderItem.getItem().getItemName();
            }

        }
        String itemName = cartNames[0]  + (orderItems.size()-1 != 0 ? "그외 " +  (orderItems.size()-1) : "");

        log.info(itemName);
        int quantity = 0;
        for (OrderItem orderItem : order.getOrderItems()) {
            quantity += orderItem.getQuantity();
        }
        int totalAmount = order.getTotalPrice() + order.getOrderPrice();
        // 카카오 결제 준비하기	- 결제요청 service 실행.
        ReadyResponseVO readyResponse = kakaopayService.payReady(order.getId(), itemName, quantity, memberId, totalAmount);

        order.setTid(readyResponse.getTid());
        orderService.update(order);
        log.info("결재고유 번호: " + readyResponse.getTid());

       session.setAttribute("order",order);

        return readyResponse; // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)
    }

    // 결제승인요청
    @GetMapping("/order/kakao/success")
    public String payCompleted(@RequestParam("pg_token") String pgToken, HttpSession session, Model model) {
        Order order = (Order)session.getAttribute("order");
        String tid = order.getTid();
        String memberId = (String)session.getAttribute("member_id");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("주문정보: " + order.getId());
        log.info("결재고유 번호: " + tid);

        // 카카오 결재 요청하기
        ApproveResponseVO approveResponse = kakaopayService.payApprove(order.getId(), tid, pgToken, memberId);
        cartService.deleteCart(memberId);
        session.removeAttribute("order");
        order.setOrderStatus(OrderStatus.PAYMENT);
        log.info("{}",order.getOrderStatus());
        orderService.update(order);
        model.addAttribute("order", order);

        return "pay/pay_confirm";
    }

    // 결제 취소시 실행 url
    @GetMapping("/order/kakao/cancel")
    public String payCancel(Long orderId) {
        Order order = orderService.findOne(orderId);
        OrderStatus orderStatus = order.getOrderStatus();
        if(orderStatus==OrderStatus.DELIVERY || orderStatus == OrderStatus.COMPLETE || orderStatus == OrderStatus.COOKING){
            return "redirect:/orders/" + orderId;
        }

        kakaopayService.payCancel(order);
        order.setOrderStatus(OrderStatus.CANCEL);
        orderService.update(order);
        return "redirect:/orders?status=cancel";
    }
//
//    // 결제 실패시 실행 url
//    @GetMapping("/order//kakaofail")
//    public String payFail() {
//        return "redirect:/order";
//    }
//

}

