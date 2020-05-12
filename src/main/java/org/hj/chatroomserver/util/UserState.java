package org.hj.chatroomserver.util;

import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.dto.UserDto;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserState {
    private static UserService userService;
    public static boolean checkLogin(){
        Object userInfo = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userInfo == null || ! (userInfo instanceof UserDto) ) {
            return false;
        }
        return true;
    }

    public static UserDto getUser(){
        Object userInfo = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userInfo == null || ! (userInfo instanceof UserDto) ) {
            throw new CustomException(CommonCode.FAIL);
        }
        return (UserDto)userInfo;
    }

    public static void setUserService(UserService userService) {
        UserState.userService = userService;
    }

    public static void refreshUser(){
        final UserDto user = getUser();

        final User rawUser = userService.findRawUser(user.getUserId());

        /**
         * 刷新用户数据
         */
        final UserDto login = BeanUtils.copyProperties(rawUser, UserDto.class);
        login.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(rawUser.getRole().toString()));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(login, auth.getCredentials(), login.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
