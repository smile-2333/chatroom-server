package org.hj.chatroomserver.util.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询注解
 * Created by star on 2017/5/8.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {

    /**
     * 查询方式
     */
    Operator operator() default Operator.EQ;

    /**
     * 查询的(对应实体)字段，不填则直接使用当前字段
     */
    String field() default "";

}
