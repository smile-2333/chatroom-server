package org.hj.chatroomserver.config.security;

import lombok.Data;

@Data
public class AuthenticationBean {
    private String username;
    private String password;
}
