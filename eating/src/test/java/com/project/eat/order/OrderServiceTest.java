package com.project.eat.order;

import com.project.eat.member.MemberService;
import com.project.eat.member.MemberVO_JPA;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@SpringBootTest
@Slf4j
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberService memberService;

    @Test
    void findByOrderType() {
        List<OrderDTO> findOrders = orderService.findByOrderType("dowon456",OrderType.TAKEOUT);
        MemberVO_JPA findMember = memberService.findOne("dowon456");
        List<Order> memberOrders = findMember.getOrders();

        List<Order> memberOrdersDelivery = new ArrayList<>();
        for (Order memberOrder : memberOrders) {
            if(memberOrder.getOrderType()==OrderType.TAKEOUT){
                memberOrdersDelivery.add(memberOrder);
            }
        }
        log.info("findOrders = {}", findOrders.size());
        log.info("memberOrdersDelivery = {}", memberOrdersDelivery.size());
        Assertions.assertThat(findOrders.size()).isEqualTo(memberOrdersDelivery.size());
    }

    @Test
    void findByItemName(){
        List<Order> findOrders = orderService.findByMemberIdByItemName("dowon456", "set");
        MemberVO_JPA findMember = memberService.findOne("dowon456");
        List<Order> memberFindOrders = findMember.getOrders().stream()
                .filter(o -> o.getOrderItems().stream().anyMatch(oi -> oi.getItem().getItemName().toLowerCase().contains("sEt".toLowerCase()))).collect(Collectors.toList());


        log.info("findOrders = {}", findOrders.size());
        log.info("memberFindOrders = {}", memberFindOrders.size());
        Assertions.assertThat(findOrders.size()).isEqualTo(memberFindOrders.size());
    }

    @Test
    void findBetweenDate(){
        LocalDate startDate = LocalDate.parse("2024-03-01");
        LocalDate endDate = LocalDate.parse("2024-04-17");
        List<Order> dowon456 = orderService.findByOrdersBetweenDates("dowon456", startDate, endDate);

        log.info("findOrders= {}", dowon456.size());

    }


    @Test
    void searchList(){
        String memberId = "dowon456";
        SearchForm form = new SearchForm();
        form.setSearchText("");
        form.setSelectedType("all");
        form.setSearchOption("all");
//        form.setPage(1);
//        form.setSearchOption("dateRange");
//        form.setStartDate(LocalDate.parse("2024-04-19"));
//        form.setEndDate(LocalDate.parse("2024-04-19"));

        List<OrderDTO> searchForm = orderService.findSearchForm(memberId, form);
        Long searchPageCount = orderService.searchPageCount(memberId, form);
        log.info("searchForm.size() = {}",searchForm.size());
        log.info("searchPageCount = {}",searchPageCount);

        for (OrderDTO order : searchForm) {
            log.info("orderId = {}",order.getId());
            log.info("orderType = {}",order.getOrderType());
            log.info("itemName = {}",order.getItemsName());
        }
    }

    @Test
    void totalCount(){
        String memberId = "dowon456";
        SearchForm form = new SearchForm();
        Long totalCount = orderService.pageCount(memberId, form);
        MemberVO_JPA findMember = memberService.findOne(memberId);
        int size = findMember.getOrders().size();

        Assertions.assertThat(totalCount).isEqualTo(size);


    }

    @Test
    void findAll(){
        String memberId = "dowon456";
        SearchForm form = new SearchForm();
        List<OrderDTO> findOrders = orderService.findAllPage(memberId, form);

        Assertions.assertThat(findOrders.size()).isEqualTo(5);
        Assertions.assertThat(findOrders.get(0).getId()).isEqualTo(161L);


    }



}