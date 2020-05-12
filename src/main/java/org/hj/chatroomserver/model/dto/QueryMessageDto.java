package org.hj.chatroomserver.model.dto;

import lombok.Data;
import org.hj.chatroomserver.util.persistence.Operator;
import org.hj.chatroomserver.util.persistence.Query;

import java.util.Date;

/**
 * @see org.hj.chatroomserver.model.entity.Message
 */
@Data
public class QueryMessageDto {
    private Date startTime;
    @Query(field = "createTime", operator = Operator.LT)
    private Date endTime;
}
