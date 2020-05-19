package org.hj.chatroomserver.config.security.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authenticationFailureHandler")
@Slf4j
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        ResponseResult responseResult = null;

        if (exception.getCause() instanceof CustomException){
            final CustomException cause = (CustomException) (exception.getCause());
            responseResult = new ResponseResult(cause.getResultCode());
        }else {
            responseResult = new ResponseResult(CommonCode.INVALID_USERNAME_OR_PASSWORD);
        }

        String result = mapper.writeValueAsString(responseResult);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(result);
        log.debug("login fail");
    }
}
