package org.hj.chatroomserver.util.persistence;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hj.chatroomserver.util.persistence.query.TimeQuery;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author nxcat
 * @date 2018-12-14
 * @see Query
 */
@Slf4j
public final class QueryHelper {

    private final static Map<Class<?>, Field[]> clazzFieldMap = new ConcurrentHashMap<>();

    /**
     * 查询注解解析
     *
     * @return
     */
    public static <T> Specification<T> parse(Object obj, Class<T> t) {
        return parseBuilder(obj, t).build();
    }


    /**
     * 解析成Builder
     *
     * @param obj
     * @param t
     * @param <T>
     * @return
     */
    public static <T> SpecificationBuilder<T> parseBuilder(Object obj, Class<T> t) {
        SpecificationBuilder<T> builder = SpecificationBuilder.of(t);

        for (Field field : getDeclaredFields(obj.getClass())) {
            Query query = field.getAnnotation(Query.class);
            if (Objects.nonNull(query)) {
                Object value = getFieldValue(field, obj);
                if (Objects.nonNull(value)) {
                    String property = query.field();
                    if (StringUtils.isBlank(property)) {
                        property = field.getName();
                    }

                    wrap(builder, query.operator(), property, value);
                }
            }
        }

        return builder;
    }


    /**
     * 时间查询处理
     *
     * @param params
     * @param builder
     * @param <T>
     */
    public static <T> void timeQuery(TimeQuery params, SpecificationBuilder<T> builder) {
        String fieldName = params.getTimeField();
        if (StringUtils.isNotBlank(fieldName)) {
            Date startTime = params.getStartTime();
            Date endTime = params.getEndTime();
            builder.between(fieldName, startTime, endTime);
        }
    }

    private static void wrap(SpecificationBuilder builder, Operator operator, String property, Object value) {

        if (value instanceof String
                && StringUtils.isBlank((String) value)) {
            // 空字符不查询
            return;
        }

        switch (operator) {
            case EQ:
                builder.equal(property, value);
                break;
            case NE:
                builder.notEqual(property, value);
                break;
            case GT:
                builder.greaterThan(property, value);
                break;
            case GTE:
                builder.greaterThanOrEqualTo(property, value);
                break;
            case LT:
                builder.lessThan(property, value);
                break;
            case LTE:
                builder.lessThanOrEqualTo(property, value);
                break;
            case LIKE:
                builder.like(property, String.valueOf(value));
                break;
            case NOTNULL:
                builder.isNotNull(property);
                break;
            case IN:
                if (value instanceof Collection) {
                    builder.in(property, (Collection) value);
                } else {
                    builder.equal(property, value);
                }
                break;
            case NOTIN:
                if (value instanceof Collection) {
                    builder.notIn(property, (Collection) value);
                } else {
                    builder.notEqual(property, value);
                }
                break;
            default:
                log.warn("Operations not resolved. operator={}, property={}, value={}", operator, property, value);
                break;
        }
    }

    private static Object getFieldValue(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            log.error("getFieldValue error", e);
        }
        return null;
    }

    private static Field[] getDeclaredFields(Class<?> cls) {
        Field[] fields = clazzFieldMap.get(cls);
        if (fields == null) {
            Class<?> clazz = cls;
            List<Field> list = new ArrayList();
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                try {
                    list.addAll(Arrays.asList(clazz.getDeclaredFields()));
                } catch (Exception e) {
                }
            }
            fields = list.stream().toArray(Field[]::new);
            clazzFieldMap.put(cls, fields);
        }
        return fields;
    }
}
