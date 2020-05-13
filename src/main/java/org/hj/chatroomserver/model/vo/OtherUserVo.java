package org.hj.chatroomserver.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OtherUserVo {

    private Integer userId;

    private String username;

    private String description;

    private String city;

    private String phone;

    private String avatar;

    private Date lastLoginTime;

    private Date lastLogoutTime;
}
