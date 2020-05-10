package org.hj.chatroomserver.exception;


import org.hj.chatroomserver.model.result.ResultCode;

public class ExceptionCast {
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
