package org.hj.chatroomserver.config.security;

import lombok.extern.slf4j.Slf4j;
import org.hj.chatroomserver.model.dto.UserDto;
import org.hj.chatroomserver.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    private final UserService userService;

    public LogoutSuccessHandlerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        final UserDto userDto = (UserDto) authentication.getPrincipal();

        userService.logout(userDto.getUserId());

        log.info(String.format("用户%s登出",userDto.getUsername()));
    }
}
