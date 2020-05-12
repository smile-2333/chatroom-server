package org.hj.chatroomserver.util.persistence;

import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kevinchen on 2017/2/7.
 */
public class Criteria<T> implements Specification<T> {

    private List<Criterion> criterionList = new ArrayList<>();
    private List<OrderBean> orders = new ArrayList<>();

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        processOrderAction(root, query);
        if (!criterionList.isEmpty()) {
            List<Predicate> predicates = new ArrayList<>();
            for (Criterion c : criterionList) {
                predicates.add(c.toPredicate(root, query, builder));
            }
            if (predicates.size() > 0) {
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }
        return builder.conjunction();
    }

    private void processOrderAction(Root<T> root, CriteriaQuery<?> query) {
        if (orders.size() == 0) {
            return;
        }
        List<Order> orderList = orders.stream().map(orderBean -> new OrderImpl(root.get(orderBean.getFieldName()), orderBean.getOrder())).collect(Collectors.toList());
        query.orderBy(orderList);
    }

    public void addOrder(String fieldName, boolean isAsc) {
        orders.add(new OrderBean(fieldName, isAsc));
    }

    public void add(Criterion criterion) {
        if (criterion != null) {
            criterionList.add(criterion);
        }
    }

    private static class OrderBean {
        private String fieldName;
        private boolean isAsc;

        public OrderBean(String fieldName, boolean isAsc) {
            this.fieldName = fieldName;
            this.isAsc = isAsc;
        }

        public String getFieldName() {
            return fieldName;
        }

        public boolean getOrder() {
            return isAsc;
        }
    }
}