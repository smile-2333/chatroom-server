package org.hj.chatroomserver.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class PrivateChat implements TimeSetting{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer privateChatId;

    @Column(nullable = false)
    private int sender;

    @Column(nullable = false)
    private int acceptorId;

    @Column(nullable = false)
    private String privateChatKey;

    private Date createTime;

    private Date updateTime;
}
