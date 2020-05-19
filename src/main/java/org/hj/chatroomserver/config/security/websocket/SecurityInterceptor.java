package org.hj.chatroomserver.config.security.websocket;

import org.hj.chatroomserver.model.dto.UserDto;
import org.hj.chatroomserver.service.UserService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class SecurityInterceptor implements ChannelInterceptor {
    private final UserService userService;

    public SecurityInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String username = accessor.getNativeHeader("username").get(0);
            String password = accessor.getNativeHeader("password").get(0);

            final UserDto login = userService.login(username,password);
            Authentication authentication = new UsernamePasswordAuthenticationToken(login,null, login.getAuthorities());
            accessor.setUser(authentication);
        }

        return message;
    }
}
