package org.hj.chatroomserver.model.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * @see org.hj.chatroomserver.model.entity.User
 */
@Data
public class UserDto implements UserDetails {

    private Integer userId;

    private String username;

    private String password;

    private String description;

    private String city;

    private String phone;

    private String avatar;

    private String email;

    private Date createTime;

    private Date updateTime;

    private Boolean isDelete;

    private Boolean isActive;

    private Date lastLoginTime;

    private Date lastLogoutTime;

    private Collection<? extends GrantedAuthority>authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
