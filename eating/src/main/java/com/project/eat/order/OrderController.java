package com.project.eat.order;

import com.project.eat.cart.Cart;
import com.project.eat.cart.CartService;
import com.project.eat.cart.cartItem.CartItemService;
import com.project.eat.item.Item;
import com.project.eat.item.itemOption.ItemOption;
import com.project.eat.member.Coupon;
import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
import com.project.eat.order.coupon.CouponForm;
import com.project.eat.order.coupon.CouponService;
import com.project.eat.order.orderItem.OrderItem;
import com.project.eat.order.orderItemOption.OrderItemOption;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final MemberService memberService;
    private final CouponService couponService;
    private final KakaoPayServiceImpl kakaopayService;


    @GetMapping("/order")
    public String orderPage(Model model, HttpSession session, OrderForm orderForm) {
        String memberId = (String)session.getAttribute("member_id");
        MemberVO_JPA findMember = memberService.findOne(memberId);
        if (findMember.getCart().getTotalPrice() < findMember.getCart().getShop().getMinPriceInt()) {
            return "redirect:/shop/" + findMember.getCart().getShop().getId();
        }
        Cart cart = findMember.getCart();

        List<Coupon> coupons = findMember.getCoupons();
        List<Coupon> filteredCoupons = coupons.stream().filter(c -> c.getShop()==null ||  c.getShop().getId().equals(cart.getShop().getId())).toList();

        model.addAttribute("coupons", filteredCoupons);
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
    public String order(HttpSession session, OrderForm form,  RedirectAttributes attributes) {
        if(form.getPaymentMethod().equals("kakaoPay")){
            attributes.addFlashAttribute("form", form);
            return "redirect:/order/kakao";
        }
        String memberId = session.getAttribute("member_id").toString();
        Coupon findCoupon = couponService.findOne(form.getCouponId());
        form.setDiscount(findCoupon.getPrice());

        // 주문 완료 오더테이블 저장하면 장바구니 삭제 쿠폰 사용했으면 삭제
        Order order = orderService.createOrder(memberId, form);
        order.setOrderStatus(OrderStatus.CHECKING);
        orderService.update(order);
        cartService.deleteCart(memberId);
        if(form.getCouponId()!=null) {
            couponService.deleteCoupon(form.getCouponId());
        }
        return "redirect:/orders";
    }


    @GetMapping("/orders")
    public String orderList(HttpSession session, Model model) {

        String memberId =(String) session.getAttribute("member_id");
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

    @GetMapping("/orders/{orderId}/reorder")
    public String reOrder(@PathVariable("orderId") Long orderId) {
        Order findOrder = orderService.findOne(orderId);
        MemberVO_JPA member = findOrder.getMember();
        if (member.getCart() == null) {
            cartService.createCart(member.getId());
            cartService.setShopCart(member.getId(), findOrder.getShop().getId());

        } else {
            cartService.deleteAndCreateCart(member.getId(), findOrder.getShop().getId());
        }
        for (OrderItem orderItem : findOrder.getOrderItems()) {
            for (OrderItemOption orderItemOption : orderItem.getOrderItemOptions()) {
                ItemOption itemOption = orderItemOption.getItemOption();
                Item item = orderItem.getItem();
                int cartPrice = item.getItemPrice() + itemOption.getPrice();
                cartItemService.saveCartItem(member.getId(), item.getId(), itemOption.getId(), orderItem.getQuantity(), cartPrice, member.getCart());
            }
        }


        return "redirect:/order";
    }

    @PostMapping("/orders/{orderId}/delete")
    public String orderDelete(@PathVariable("orderId") Long orderId) {
        Order order = orderService.findOne(orderId);
        if (order.getOrderStatus() == OrderStatus.COMPLETE) {
            orderService.deleteOne(orderId);
        }
        return "redirect:/orders";
    }

    @GetMapping("/order/kakao")
    public String payReady(Model model, HttpSession session) {
        OrderForm form =(OrderForm) model.getAttribute("form");
        String memberId = session.getAttribute("member_id").toString();
        Coupon findCoupon = couponService.findOne(form.getCouponId());
        form.setDiscount(findCoupon.getPrice());

        Order order = orderService.createOrder(memberId, form);
        List<OrderItem> orderItems = order.getOrderItems();
        String[] cartNames = new String[orderItems.size()];
        for (OrderItem orderItem : orderItems) {
            for (int i = 0; i < orderItems.size(); i++) {
                cartNames[i] = orderItem.getItem().getItemName();
            }

        }
        String itemName = cartNames[0] + (orderItems.size() - 1 != 0 ? "그외 " + (orderItems.size() - 1) : "");

        log.info(itemName);
        int quantity = 0;
        for (OrderItem orderItem : order.getOrderItems()) {
            quantity += orderItem.getQuantity();
        }
        int totalAmount = order.getTotalPrice() + order.getOrderPrice() - order.getDiscount();
        // 카카오 결제 준비하기	- 결제요청 service 실행.
        ReadyResponseVO readyResponse = kakaopayService.payReady(order.getId(), itemName, quantity, memberId, totalAmount);

        order.setTid(readyResponse.getTid());
        orderService.update(order);
        log.info("결재고유 번호: " + readyResponse.getTid());

        session.setAttribute("order", order);
        session.setAttribute("couponId", form.getCouponId());
        log.info("{}",readyResponse.getNext_redirect_pc_url());
        return "redirect:"+readyResponse.getNext_redirect_pc_url(); // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)
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
        Long couponId = (Long) session.getAttribute("couponId");
        couponService.deleteCoupon(couponId);
        session.removeAttribute("couponId");
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
@PostMapping("/order/coupon")
@ResponseBody
public ResponseEntity<Map<String, Integer>> getCouponDiscount(@RequestBody CouponForm form, HttpSession session) {
    Map<String, Integer> response = new HashMap<>();
    String memberId =(String) session.getAttribute("memberId");
    MemberVO_JPA findMember = memberService.findOne(memberId);
    response.put("totalPrice", findMember.getCart().getTotalPrice());
    response.put("deliveryPrice", findMember.getCart().getShop().getDeliveryPrice());

    List<Coupon> coupons = findMember.getCoupons();
    log.info("coupons = {}", coupons);
    if (coupons != null) {
        for (Coupon coupon : coupons) {
            if (coupon.getId().equals(form.getCouponId())) {
                log.info("{}", coupon.getPrice());
                response.put("discount", coupon.getPrice());
                return ResponseEntity.ok().body(response); // Explicitly mentioning .body() for clarity
            }
        }
    }
    return ResponseEntity.notFound().build();
}

}

