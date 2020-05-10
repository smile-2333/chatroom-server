package org.hj.chatroomserver.model.result;

public interface ResultCode {
    /**
     * 是否成功
     * @return
     */
    boolean success();

    /**
     * 操作代码
     * @return
     */
    int code();

    /**
     * 提示信息
     * @return
     */
    String message();
}
