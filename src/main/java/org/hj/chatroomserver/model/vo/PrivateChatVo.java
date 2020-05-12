package org.hj.chatroomserver.model.vo;

import lombok.Data;
import org.hj.chatroomserver.model.entity.User;

import java.util.Date;

/**
 * @see org.hj.chatroomserver.model.entity.PrivateChat
 */
@Data
public class PrivateChatVo {

    private Integer privateChatId;

    private Integer senderId;

    private Integer receiverId;

    private Date createTime;

    private Date updateTime;

    private User sender;

    private User receiver;
}
