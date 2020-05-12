package org.hj.chatroomserver.model.result;

public enum CommonCode implements ResultCode{
    SUCCESS(true,100000,"成功"),
    FAIL(false,100001,"失败"),
    INVALID_USERNAME_OR_PASSWORD(false,100002,"账号或者密码错误"),
    SEND_EMAIL_FAIL(false,100003,"发送邮件失败"),
    MAIL_EXPIRED(false,100004,"邮件已经过期，请重新发送"),
    USER_NOT_FIND(false,100005,"不存在该用户"),
    ACTIVATE_SUCCESS(true,100006,"激活成功"),
    ACCOUNT_NOT_ACTIVE(false,100007,"账户尚未激活"),
    UPLOAD_IMAGE_FAIL(false,100008,"上传图片失败"),
    USERNAME_BEEN_USED(false,100010,"用户名已被占用"),
    EMAIL_BEEN_USED(false,100011,"邮箱已被占用"),
    MESSAGE_SENT_ERROR(false,100012,"消息发送失败"),
    CONVERT_ERROR(false,100013,"类型转换失败"),
    JPA_PARSER_ERROR(false,100014,"JPA转换失败"),
    HANDLE_MESSAGE_ERROR(false,100015,"消息处理失败"),
    PRIVATE_CHAT_ESTABLISH_FAIL(false,100016,"私聊建立失败"),
    ACCOUNT_NOT_EXIST(false,100017,"账户不存在"),
    PRIVATE_CHAT_REMOVE_FAIL(false,100018,"拆除私聊失败"),
    PRIVATE_CHAT_NOT_EXIST(false,100019,"私聊不存在"),
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
