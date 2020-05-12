package org.hj.chatroomserver.util.persistence;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * JPA 查询规范构建
 * 查询条件快速构建
 *
 * @param <T>
 */
public class SpecificationBuilder<T> {

    private Criteria<T> criteria;

    private SpecificationBuilder() {
        this.criteria = new Criteria<>();
    }

    /**
     * 创建Builder
     * <p>
     * 代替方法 com.cdy.base.utils.persistence.SpecificationBuilder#of(java.lang.Class)
     */
    @Deprecated
    public static <S> SpecificationBuilder<S> create() {
        return new SpecificationBuilder<>();
    }

    /**
     * 创建Builder
     *
     * @param <S>
     * @return
     */
    public static <S> SpecificationBuilder<S> of(Class<S> s) {
        return new SpecificationBuilder<>();
    }

    /**
     * 构建
     *
     * @return
     */
    public Specification <T> build() {
        return this.criteria;
    }

    /**
     * 排序
     *
     * @param fieldName
     * @param isAsc
     * @return
     */
    public SpecificationBuilder<T> order(String fieldName, boolean isAsc) {
        this.criteria.addOrder(fieldName, isAsc);
        return this;
    }

    /**
     * 顺序
     *
     * @param fieldName
     * @return
     */
    public SpecificationBuilder<T> orderAsc(String fieldName) {
        return order(fieldName, true);
    }

    /**
     * 倒序
     *
     * @param fieldName
     * @return
     */
    public SpecificationBuilder<T> orderDesc(String fieldName) {
        return order(fieldName, false);
    }

    /**
     * 等于
     *
     * @param fieldName
     * @param value
     * @return
     */
    public SpecificationBuilder<T> equal(String fieldName, Object value) {
        this.criteria.add(Restrictions.eq(fieldName, value));
        return this;
    }

    /**
     * 不等于
     *
     * @param fieldName
     * @param value
     * @return
     */
    public SpecificationBuilder<T> notEqual(String fieldName, Object value) {
        this.criteria.add(Restrictions.ne(fieldName, value));
        return this;
    }

    /**
     * 不为null
     *
     * @param fieldName
     * @return
     */
    public SpecificationBuilder<T> isNotNull(String fieldName) {
        this.criteria.add(Restrictions.notNull(fieldName));
        return this;
    }

    /**
     * 为null
     *
     * @param fieldName
     * @return
     */
    public SpecificationBuilder<T> isNull(String fieldName) {
        this.criteria.add(Restrictions.isNull(fieldName));
        return this;
    }


    /**
     * 小于
     *
     * @param fieldName
     * @param value
     * @return
     */
    public SpecificationBuilder<T> lessThan(String fieldName, Object value) {
        this.criteria.add(Restrictions.lt(fieldName, value));
        return this;
    }

    /**
     * 大于
     *
     * @param fieldName
     * @param value
     * @return
     */
    public SpecificationBuilder<T> greaterThan(String fieldName, Object value) {
        this.criteria.add(Restrictions.gt(fieldName, value));
        return this;
    }

    /**
     * 小于等于
     *
     * @param fieldName
     * @param value
     * @return
     */
    public SpecificationBuilder<T> lessThanOrEqualTo(String fieldName, Object value) {
        this.criteria.add(Restrictions.lte(fieldName, value));
        return this;
    }

    /**
     * 大于等于
     *
     * @param fieldName
     * @param value
     * @return
     */
    public SpecificationBuilder<T> greaterThanOrEqualTo(String fieldName, Object value) {
        this.criteria.add(Restrictions.gte(fieldName, value));
        return this;
    }

    /**
     * in
     *
     * @param fieldName
     * @param values
     * @return
     */
    public SpecificationBuilder<T> in(String fieldName, Collection values) {
        if (!CollectionUtils.isEmpty(values)) {
            this.criteria.add(Restrictions.in(fieldName, values));
        }
        return this;
    }

    /**
     * notIn
     *
     * @param fieldName
     * @param values
     * @return
     */
    public SpecificationBuilder<T> notIn(String fieldName, Collection values) {
        if (!CollectionUtils.isEmpty(values)) {
            this.criteria.add(Restrictions.notIn(fieldName, values));
        }
        return this;
    }

    /**
     * 模糊查询
     *
     * @param fieldName
     * @param value
     * @return
     */
    public SpecificationBuilder<T> like(String fieldName, String value) {
        this.criteria.add(Restrictions.like(fieldName, value));
        return this;
    }

    /**
     * 范围查询
     *
     * @param fieldName
     * @param lowerBound 小值
     * @param upperBound 大值
     * @return
     */
    public SpecificationBuilder<T> between(String fieldName, Comparable lowerBound, Comparable upperBound) {
        this.criteria.add(Restrictions.between(fieldName, lowerBound, upperBound));
        return this;
    }
}
