package org.hj.chatroomserver.config.security.http;

import lombok.Data;

@Data
public class AuthenticationBean {
    private String username;
    private String password;
}
