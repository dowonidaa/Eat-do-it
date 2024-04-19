package com.project.eat.order;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderDAO_JPA extends JpaRepository<Order, Object> {



    @Query("select o from Order o where o.member.id = :memberId and o.orderType = :orderType order by o.orderDate desc ")
    List<Order> findByMemberIdByOrderType(@Param("memberId") String memberId, @Param("orderType") OrderType orderType);

    @Query("SELECT o FROM Order o JOIN o.orderItems oi WHERE o.member.id = :memberId and lower(oi.item.itemName) LIKE lower(concat('%', :itemName, '%'))")
    List<Order> findByItemNameContainingIgnoreCase(@Param("memberId")String memberId, @Param("itemName") String itemName);

    @Query("SELECT o FROM Order o WHERE o.member.id = :memberId AND CAST(o.orderDate AS DATE) BETWEEN :startDate AND :endDate ")
    List<Order> findByOrdersBetweenDates(@Param("memberId") String memberId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT o FROM Order o JOIN o.orderItems oi WHERE o.member.id = :memberId AND o.orderType = :orderType AND lower(oi.item.itemName) LIKE lower(concat('%', :itemName, '%')) order by o.Id desc ")
    List<Order> findByItemNameContainingIgnoreCaseAndOrderType(@Param("memberId") String memberId, @Param("itemName") String itemName, @Param("orderType") OrderType orderType);

    @Query("SELECT o FROM Order o WHERE o.member.id = :memberId AND o.orderType = :orderType AND CAST(o.orderDate AS DATE) BETWEEN :startDate AND :endDate order by o.Id desc ")
    List<Order> findByOrdersBetweenDatesAndOrderType(@Param("memberId") String memberId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("orderType") OrderType orderType);
}
