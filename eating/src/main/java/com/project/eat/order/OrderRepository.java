package com.project.eat.order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }


    public Order findOne(Long orderId) {
        return em.find(Order.class, orderId);
    }

    public void deleteOne(Order one) {
        em.remove(one);
    }

    public List<Order> searchListBetweenDates(String memberId, SearchForm form) {
        String jpql = "select distinct o from Order o join o.shop s join o.orderItems oi join oi.item i where o.member.id = :memberId and (lower(s.shopName) like lower(:searchText) or lower(i.itemName) like lower(:searchText))";

        if (!form.getSelectedType().equals("all")) {
            jpql += " and o.orderType = :orderType";
        }

        if (form.getSearchOption().equals("dateRange")) {
            jpql += "  and cast(o.orderDate as date) between :startDate and :endDate";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                            .setParameter("memberId", memberId)
                            .setParameter("searchText", "%"+form.getSearchText()+"%");

        if (!form.getSelectedType().equals("all")) {
            query.setParameter("orderType", OrderType.valueOf(form.getSelectedType().toUpperCase()));
        }
        if(form.getSearchOption().equals("dateRange")) {
            query.setParameter("startDate", form.getStartDate())
                    .setParameter("endDate", form.getEndDate());
        }

        query.setFirstResult(0);
        query.setMaxResults(10);
        return query.getResultList();
    }




}
