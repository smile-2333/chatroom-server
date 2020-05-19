package org.hj.chatroomserver.model.vo;

import lombok.Data;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.enums.ContextType;
import org.hj.chatroomserver.model.enums.ResourceType;

import java.util.Date;

/**
 * @see org.hj.chatroomserver.model.entity.Message
 * @see org.hj.chatroomserver.model.entity.User
 */
@Data
public class MessageVo {

    private Integer messageId;

    private Integer senderId;

    private String content;

    private ContextType contextType;

    private Date createTime;

    private Date updateTime;

    private String avatar;

    private String username;

    private User sender;

    private User receiver;

    private ResourceType resourceType;

    private String fileName;
}
