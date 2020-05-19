package org.hj.chatroomserver.model.dto;

import lombok.Data;
import org.hj.chatroomserver.model.enums.ContextType;
import org.hj.chatroomserver.util.persistence.Operator;
import org.hj.chatroomserver.util.persistence.Query;

import java.util.Date;
import java.util.List;

/**
 * @see org.hj.chatroomserver.model.entity.Message
 */
@Data
public class QueryMessageDto {
    @Query(field = "createTime", operator = Operator.GT)
    private Date createTimeStart;

    @Query(field = "createTime", operator = Operator.LTE)
    private Date createTimeEnd;

    @Query(field = "senderId",operator = Operator.EQ)
    private Integer senderId;

    @Query(field = "receiverId",operator = Operator.EQ)
    private Integer receiverId;

    @Query
    private ContextType contextType;

    private List<String> fileNames;

    private List<String> contents;

    /**
     * 用于用户自己查相关记录用，所以是双向查找,与上述的senderId/receiverId互斥
     */
    private Integer senderIdOrReceiverId;

    /**
     * 同上
     */
    private Integer receiverIdOrSenderId;

    private String senderName;

    private String receiverName;
}
