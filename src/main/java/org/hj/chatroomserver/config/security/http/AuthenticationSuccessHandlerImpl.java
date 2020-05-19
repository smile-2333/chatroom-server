package org.hj.chatroomserver.config.security.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hj.chatroomserver.model.dto.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authenticationSuccessHandler")
@Slf4j
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public AuthenticationSuccessHandlerImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object userInfo = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userInfo != null) {
            if (userInfo instanceof UserDto){

                String result = objectMapper.writeValueAsString(userInfo);
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(result);
            }
        }

        log.debug("login success");
    }
}
