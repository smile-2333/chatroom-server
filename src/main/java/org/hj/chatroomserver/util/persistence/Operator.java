package org.hj.chatroomserver.util.persistence;

/**
 * Created by kevinchen on 2017/2/8.
 */
public enum Operator {

    /**
     * 等于
     */
    EQ,

    /**
     * 不等于
     */
    NE,

    /**
     * 模糊查询
     */
    LIKE,

    /**
     * 大于
     */
    GT,

    /**
     * 小于
     */
    LT,

    /**
     * 大于等于
     */
    GTE,

    /**
     * 小于等于
     */
    LTE,

    /**
     * 包含
     */
    IN,

    /**
     * 不包含
     */
    NOTIN,

    /**
     * 不为NULL
     */
    NOTNULL,

    /**
     * 为NULL
     */
    ISNULL,

    /**
     * 之间
     */
    BETWEEN,

    /**
     * 并且
     */
    AND,

    /**
     * 或者
     */
    OR,

}
