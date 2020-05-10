package org.hj.chatroomserver.exception;


import org.hj.chatroomserver.model.result.ResultCode;

public class CustomException extends RuntimeException {
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
