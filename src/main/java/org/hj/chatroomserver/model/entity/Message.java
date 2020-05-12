package org.hj.chatroomserver.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hj.chatroomserver.model.enums.ContextType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;


@DynamicInsert
@DynamicUpdate
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    /**
     * 发送者Id
     */
    @Column(nullable = false)
    private Integer senderId;

    /**
     * 接收者Id，-1代表没有，即聊天室，其他代表对应发送信息
     */
    @Column(nullable = false)
    private Integer receiverId;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContextType contextType;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;
}
