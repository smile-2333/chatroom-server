package org.hj.chatroomserver.model.dto;

import lombok.Data;
import org.hj.chatroomserver.util.persistence.Operator;
import org.hj.chatroomserver.util.persistence.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class QueryUserDto {
    @Query(field = "userId",operator = Operator.EQ)
    private Integer userId;

    @Query(field = "username",operator = Operator.LIKE)
    private String username;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(field = "createTime", operator = Operator.GT)
    private Date createTimeStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(field = "createTime", operator = Operator.LTE)
    private Date createTimeEnd;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(field = "updateTime", operator = Operator.GT)
    private Date updateTimeStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Query(field = "updateTime", operator = Operator.LTE)
    private Date updateTimeEnd;

}
