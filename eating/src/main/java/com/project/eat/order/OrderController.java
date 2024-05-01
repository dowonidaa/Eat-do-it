package com.project.eat.order;

import com.project.eat.address.AddService;
import com.project.eat.address.AddressVO_JPA;
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
import com.project.eat.order.kakaopay.ApproveResponseVO;
import com.project.eat.order.kakaopay.KakaoPayServiceImpl;
import com.project.eat.order.kakaopay.ReadyResponseVO;
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
    private final AddService addService;


    @GetMapping("/order")
    public String orderPage(Model model, HttpSession session, OrderForm orderForm) {
        String memberId = (String) session.getAttribute("member_id");
        MemberVO_JPA findMember = memberService.findOne(memberId);
        if (findMember.getCart().getTotalPrice() < findMember.getCart().getShop().getMinPriceInt()) {
            return "redirect:/shopDetail?num=" + findMember.getCart().getShop().getShopId();
        }
        Cart cart = findMember.getCart();

        List<Coupon> coupons = findMember.getCoupons();
        List<Coupon> filteredCoupons = coupons.stream().filter(c -> c.isCanUse() && (c.getShop() == null || c.getShop().getShopId().equals(cart.getShop().getShopId()))).toList();
        AddressVO_JPA memberAddress = addService.findByMemberId(findMember);
        model.addAttribute("coupons", filteredCoupons);
        model.addAttribute("cart", cart);
        model.addAttribute("member", findMember);
        model.addAttribute("orderForm", orderForm);
        model.addAttribute("address", memberAddress);
        log.info("/order orderType={}", orderForm.getOrderType());

        return "pay/orderPage";
    }

    @PostMapping("/order")
    public String order(HttpSession session, OrderForm form, RedirectAttributes attributes) {
        if (form.getPaymentMethod().equals("kakaoPay")) {
            attributes.addFlashAttribute("form", form);
            return "redirect:/order/kakao";
        }
        String memberId = (String) session.getAttribute("member_id");
        if (form.getCouponId() != null) {
            Coupon findCoupon = couponService.findOne(form.getCouponId());
            form.setDiscount(findCoupon.getPrice());
        }

        // 주문 완료 오더테이블 저장하면 장바구니 삭제 쿠폰 사용했으면 삭제
        Order order = orderService.createOrder(memberId, form);
        order.setOrderStatus(OrderStatus.CHECKING);
        orderService.update(order);
        cartService.deleteCart(memberId);
        if (form.getCouponId() != null) {
            couponService.couponUsed(form.getCouponId(), order);
        }
        return "redirect:/order/success?orderId=" + order.getId();
    }

    @GetMapping("/order/success")
    public String success(Long orderId, Model model) {
        Order order = orderService.findOne(orderId);
        model.addAttribute("order", order);
        return "pay/pay_confirm";
    }


    @GetMapping("/orders")
    public String orderList(HttpSession session, Model model, @ModelAttribute("message") String message, SearchForm form) {

        String memberId = (String) session.getAttribute("member_id");
        List<OrderDTO> ordersDto =  orderService.findAllPage(memberId, form);
        Long pageCount = orderService.pageCount(memberId, form);

        model.addAttribute("ordersDto", ordersDto);
        model.addAttribute("pageCount", pageCount);
        Long endPage = ((long) ((form.getPage() - 1) / 5 + 1) * 5);
        Long startPage = ((long) ((form.getPage() - 1) / 5) * 5 +1 );
        if(pageCount < endPage){
            endPage = pageCount;
        }
        Page page = new Page(startPage, endPage, pageCount);

        model.addAttribute("page", page);

        return "order/orderList";
    }

    @PostMapping("/orders/search")
    public String search(SearchForm form, HttpSession session, Model model) {

        String memberId = (String) session.getAttribute("member_id");
        log.info("searchForm = {}", form);
        List<OrderDTO> findOrders = orderService.findSearchForm(memberId, form);
        Long searchPageCount = orderService.searchPageCount(memberId, form);

        model.addAttribute("ordersDto", findOrders);
        model.addAttribute("pageCount", searchPageCount);
        Long endPage = ((long) ((form.getPage() - 1) / 5 + 1) * 5);
        Long startPage = ((long) ((form.getPage() - 1) / 5) * 5 +1 );
        if(searchPageCount < endPage){
            endPage = searchPageCount;
        }
        Page page = new Page(startPage, endPage, searchPageCount);

        model.addAttribute("page", page);


        return "order/ordersearchForm";
    }


    @GetMapping("/orders/{orderId}")
    public String orderDetail(@PathVariable("orderId") Long orderId, HttpSession session, Model model, @ModelAttribute("message") String message) {
        Order findOrder = orderService.findOne(orderId);
        if (!findOrder.getMember().getId().equals(session.getAttribute("member_id"))) {
            return "redirect:/";
        }
        log.info("orderStatus = {}", findOrder.getOrderStatus());
        model.addAttribute("order", findOrder);
        List<OrderItem> orderItems = findOrder.getOrderItems();
        String itemName = orderItems.get(0).getItem().getItemName() + (orderItems.size() - 1 != 0 ? " 외 " + (orderItems.size() - 1) + "개" : "");
        model.addAttribute("orderItems", itemName);

        log.info(message);
        return "order/orderDetail";
    }

    @GetMapping("/orders/{orderId}/reorder")
    public String reOrder(@PathVariable("orderId") Long orderId) {
        Order findOrder = orderService.findOne(orderId);
        MemberVO_JPA member = findOrder.getMember();
        if (member.getCart() == null) {
            cartService.createCart(member.getId());
            cartService.setShopCart(member.getId(), findOrder.getShop().getShopId());

        } else {
            cartService.deleteAndCreateCart(member.getId(), findOrder.getShop().getShopId());
        }
        for (OrderItem orderItem : findOrder.getOrderItems()) {
            for (OrderItemOption orderItemOption : orderItem.getOrderItemOptions()) {
                ItemOption itemOption = orderItemOption.getItemOption();
                Item item = orderItem.getItem();
                int cartPrice = item.getItemPrice() + itemOption.getPrice();
                cartItemService.saveCartItem(member.getId(), item.getId(), itemOption.getId(), orderItem.getQuantity(), cartPrice, member.getCart());
            }
        }


        return "redirect:/order?orderType=" + findOrder.getOrderType();
    }

    @GetMapping("/orders/{orderId}/delete")
    public String orderDelete(@PathVariable("orderId") Long orderId, HttpSession session, RedirectAttributes attributes) {
        String memberId = (String) session.getAttribute("member_id");
        Order order = orderService.findOne(orderId);
        if (order.getMember().getId().equals(memberId)) {
            if (order.getOrderStatus() == OrderStatus.COMPLETE || order.getOrderStatus() == OrderStatus.CANCEL) {
                attributes.addFlashAttribute("message", "주문내역이 삭제 되었습니다.");
                orderService.deleteOne(orderId);
                return "redirect:/orders";
            }
        }
        attributes.addFlashAttribute("message", "(" + order.getOrderStatus().toString() + ")" + " 상태 입니다. 주문 취소가 불가능 합니다.");
        return "redirect:/orders/{orderId}";
    }

    @GetMapping("/order/kakao")
    public String payReady(Model model, HttpSession session) {
        OrderForm form = (OrderForm) model.getAttribute("form");
        String memberId = session.getAttribute("member_id").toString();
//        log.info("coupon = {}", form.getCouponId());
        if(form.getCouponId()!=null) {
            Coupon findCoupon = couponService.findOne(form.getCouponId());
            form.setDiscount(findCoupon.getPrice());
        }
        Order order = orderService.createOrder(memberId, form);
        List<OrderItem> orderItems = order.getOrderItems();
        String[] cartNames = new String[orderItems.size()];
        for (OrderItem orderItem : orderItems) {
            for (int i = 0; i < orderItems.size(); i++) {
                cartNames[i] = orderItem.getItem().getItemName();
            }

        }
        String itemName = cartNames[0] + (orderItems.size() - 1 != 0 ? " 외 " + (orderItems.size() - 1) : "");

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
        if (form.getCouponId()!=null) {
            session.setAttribute("couponId", form.getCouponId());
        }
        log.info("{}", readyResponse.getNext_redirect_pc_url());
        return "redirect:" + readyResponse.getNext_redirect_pc_url(); // 클라이언트에 보냄.(tid,next_redirect_pc_url이 담겨있음.)
    }

    // 결제승인요청
    @GetMapping("/order/kakao/success")
    public String payCompleted(@RequestParam("pg_token") String pgToken, HttpSession session, Model model) {
        Order order = (Order) session.getAttribute("order");
        String tid = order.getTid();
        String memberId = (String) session.getAttribute("member_id");
        log.info("결제승인 요청을 인증하는 토큰: " + pgToken);
        log.info("주문정보: " + order.getId());
        log.info("결재고유 번호: " + tid);

        // 카카오 결재 요청하기
        ApproveResponseVO approveResponse = kakaopayService.payApprove(order.getId(), tid, pgToken, memberId);
        cartService.deleteCart(memberId);

        Long couponId = (Long) session.getAttribute("couponId");
        if(couponId!=null) {
            couponService.couponUsed(couponId, order);
            session.removeAttribute("couponId");
        }
        session.removeAttribute("order");
        order.setOrderStatus(OrderStatus.PAYMENT);
        log.info("{}", order.getOrderStatus());
        orderService.update(order);
        model.addAttribute("order", order);

        return "pay/pay_confirm";
    }

    // 결제 취소시 실행 url
    @GetMapping("/order/kakao/cancel")
    public String payCancel(Long orderId, RedirectAttributes attributes) {
        Order order = orderService.findOne(orderId);
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus == OrderStatus.DELIVERY || orderStatus == OrderStatus.COMPLETE || orderStatus == OrderStatus.COOKING) {
            attributes.addFlashAttribute("message", "(" + orderStatus + ")" + " 상태 입니다. 주문 취소가 불가능 합니다.");
            return "redirect:/orders/" + orderId;
        }
        if (orderStatus == OrderStatus.READY) {
            return "redirect:/order/cancel?orderId=" + orderId;
        }

        kakaopayService.payCancel(order);
        order.setOrderStatus(OrderStatus.CANCEL);
        orderService.update(order);
        if(order.getCoupon()!=null) {
            couponService.couponRollback(order);
        }
        attributes.addFlashAttribute("message", "주문이 취소 되었습니다.");
        return "redirect:/orders/" + orderId;
    }

    @GetMapping("/order/cancel")
    public String cancel(Long orderId, RedirectAttributes attributes) {
        Order order = orderService.findOne(orderId);
        OrderStatus orderStatus = order.getOrderStatus();
        if (orderStatus == OrderStatus.DELIVERY || orderStatus == OrderStatus.COMPLETE || orderStatus == OrderStatus.COOKING) {
            attributes.addFlashAttribute("message", "(" + orderStatus + ")" + " 상태 입니다. 주문 취소가 불가능 합니다.");
            return "redirect:/orders/" + orderId;
        }
        if(order.getCoupon() !=null) {
            couponService.couponRollback(order);
        }
        order.setOrderStatus(OrderStatus.CANCEL);
        orderService.update(order);
        attributes.addFlashAttribute("message", "주문이 취소 되었습니다.");
        return "redirect:/orders/" + orderId;
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
        String memberId = (String) session.getAttribute("member_id");
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
                    return ResponseEntity.ok().body(response);
                }
            }
        }

        return ResponseEntity.notFound().build();
    }

}

