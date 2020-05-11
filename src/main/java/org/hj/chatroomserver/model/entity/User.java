package org.hj.chatroomserver.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hj.chatroomserver.model.enums.Role;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@Entity
@Data
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String city;

    private String phone;

    @Column(nullable = false)
    private String avatar;

    @Column(unique = true,nullable = false)
    private String email;

    @CreatedDate
    private Date createTime;

    @LastModifiedDate
    private Date updateTime;


    @Column(columnDefinition = "tinyint default 0")
    private Boolean isDelete;

    @Column(columnDefinition = "tinyint default 0")
    private Boolean isActive;

}
