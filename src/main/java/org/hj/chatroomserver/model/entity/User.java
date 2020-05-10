package org.hj.chatroomserver.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@Entity
@Data
public class User implements TimeSetting{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true,nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String description;

    @Column(nullable = false)
    private String role;

    private String city;

    private String phone;

    @Column(nullable = false)
    private String avatar;

    @Column(unique = true,nullable = false)
    private String email;

    private Date createTime;

    private Date updateTime;

}
