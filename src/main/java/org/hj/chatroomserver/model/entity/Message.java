package org.hj.chatroomserver.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hj.chatroomserver.model.enums.ContextType;

import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@Entity
@Data
public class Message implements TimeSetting{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer messageId;

    @Column(nullable = false)
    private int ownerId;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContextType contextType;

    private Date createTime;

    private Date updateTime;
}
