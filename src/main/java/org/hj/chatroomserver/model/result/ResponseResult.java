package org.hj.chatroomserver.model.result;

import lombok.Data;

@Data
public class ResponseResult {
    private boolean success;

    private int code;

    private String message;

    public ResponseResult(ResultCode resultCode){
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static ResponseResult SUCCESS(){
        return new ResponseResult(CommonCode.SUCCESS);
    }


    public static ResponseResult SUCCESS(String message){
        ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
        responseResult.setMessage(message);
        return responseResult;
    }

    public static ResponseResult FAIL(){
        return new ResponseResult(CommonCode.FAIL);
    }

    public static ResponseResult FAIL(String message){
        ResponseResult fail = FAIL();
        fail.setMessage(message);
        return fail;
    }
}
