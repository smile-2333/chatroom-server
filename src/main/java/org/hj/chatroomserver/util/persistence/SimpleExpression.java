package org.hj.chatroomserver.util.persistence;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by kevinchen on 2017/2/8.
 */
public class SimpleExpression implements Criterion {

    private String fieldName;       // 属性名
    private Operator operator;      // 计算符

    private Object value;           // 对应值

    private Comparable lowerBound;  // 范围查询[小的值]
    private Comparable upperBound;  // 范围查询[大的值]


    public SimpleExpression(String fieldName, Operator operator) {
        this.fieldName = fieldName;
        this.operator = operator;
    }

    public SimpleExpression(String fieldName, Object value, Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public SimpleExpression(Comparable lowerBound, Comparable upperBound, String fieldName) {
        this.fieldName = fieldName;
        this.operator = Operator.BETWEEN;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }


    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        Path expression;
        if (fieldName.contains(".")) {
            String[] names = StringUtils.split(fieldName, ".");
            expression = root.get(names[0]);
            for (int i = 1; i < names.length; i++) {
                expression = expression.get(names[i]);
            }
        } else {
            expression = root.get(fieldName);
        }

        switch (operator) {
            case EQ:
                return builder.equal(expression, value);
            case NE:
                return builder.notEqual(expression, value);
            case LIKE:
                return builder.like(expression, "%" + value + "%");
            case LT:
                return builder.lessThan(expression, (Comparable) value);
            case GT:
                return builder.greaterThan(expression, (Comparable) value);
            case LTE:
                return builder.lessThanOrEqualTo(expression, (Comparable) value);
            case GTE:
                return builder.greaterThanOrEqualTo(expression, (Comparable) value);
            case NOTNULL:
                return builder.isNotNull(expression);
            case ISNULL:
                return builder.isNull(expression);
            case BETWEEN:
                return builder.between(expression, lowerBound, upperBound);
            case IN:
                return getIn(builder, expression);
            case NOTIN:
                return builder.not(getIn(builder, expression));
            default:
                return null;
        }
    }

    private CriteriaBuilder.In getIn(CriteriaBuilder builder, Path expression) {
        CriteriaBuilder.In in = builder.in(expression);
        if (value instanceof Collection) {
            Iterator iterator = ((Collection) value).iterator();
            while (iterator.hasNext()) {
                in.value(iterator.next());
            }
        } else {
            in.value(value);
        }
        return in;
    }

}
