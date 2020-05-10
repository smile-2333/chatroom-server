package org.hj.chatroomserver.util;

import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.result.CommonCode;

public class BeanUtils {
    public  static <T> T copyProperties(Object source, Class<T>tClass){
        try {
            final T target = tClass.getConstructor().newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source,target);
            return target;
        }catch (Exception ex){

        }
        throw new CustomException(CommonCode.FAIL);
    }
}
