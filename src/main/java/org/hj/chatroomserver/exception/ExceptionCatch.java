package org.hj.chatroomserver.exception;

import org.hj.chatroomserver.model.result.ResponseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionCatch {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult catchCustomException(CustomException ce){
        return new ResponseResult(ce.getResultCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult catchNotCustomException(Exception ce){
        return ResponseResult.FAIL(ce.getMessage());
    }

}
