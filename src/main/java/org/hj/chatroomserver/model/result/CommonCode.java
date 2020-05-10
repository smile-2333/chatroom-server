package org.hj.chatroomserver.model.result;

public enum CommonCode implements ResultCode{
    SUCCESS(true,100000,"成功"),
    FAIL(false,100001,"失败"),
    INVALID_USERNAME_OR_PASSWORD(false,100002,"账号或者密码错误")
    ;

    boolean success;

    int code;

    String message;

    CommonCode(boolean success, int code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return this.success;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}
