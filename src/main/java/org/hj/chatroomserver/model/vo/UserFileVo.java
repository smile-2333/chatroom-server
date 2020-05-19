package org.hj.chatroomserver.model.vo;

import lombok.Data;
import org.hj.chatroomserver.model.enums.ResourceType;

import java.util.Date;

/**
 * @see org.hj.chatroomserver.model.entity.Message
 */
@Data
public class UserFileVo {
    private Integer messageId;

    private Integer senderId;

    private String content;

    private Date createTime;

    private Date updateTime;

    private ResourceType resourceType;

    private String fileName;
}
