package org.hj.chatroomserver.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
public class PrivateChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer privateChatId;

    @Column(nullable = false)
    private Integer senderId;

    @Column(nullable = false)
    private Integer receiverId;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;
}
