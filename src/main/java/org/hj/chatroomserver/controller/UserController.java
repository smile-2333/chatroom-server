package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.dto.QueryUserDto;
import org.hj.chatroomserver.model.dto.ResetPasswordDto;
import org.hj.chatroomserver.model.dto.UpdateUserDto;
import org.hj.chatroomserver.model.dto.UserDto;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.model.vo.OtherUserVo;
import org.hj.chatroomserver.service.UserService;
import org.hj.chatroomserver.util.UserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("activate")
    public ResponseResult activate(String code){
        return userService.activate(code);
    }

    @PostMapping("avatar")
    public ResponseResult updateAvatar(@RequestParam("file") MultipartFile file){
        return userService.updateAvatar(UserState.getUser().getUserId(),file);
    }

    @GetMapping
    public UserDto findByUserId(){
        return UserState.getUser();
    }

    @GetMapping("{userId}")
    public OtherUserVo findOtherByUserId(@PathVariable("userId")int userId){
        return userService.findOtherByUserId(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("users")
    public Page<OtherUserVo> findUsers(QueryUserDto queryUserDto, Pageable pageable){
        return userService.getUsers(queryUserDto,pageable);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("freeze/{userId}")
    public ResponseResult freezeUser(@PathVariable("userId")int userId){
        return userService.freezeUser(userId);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{userId}")
    public ResponseResult deleteUser(@PathVariable("userId")int userId){
        return userService.deleteUser(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("batch")
    public ResponseResult deleteUser(int[]userIds){
        return userService.deleteUser(userIds);
    }

    @PostMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto user){
        user.setUserId(UserState.getUser().getUserId());
        return userService.updateUser(user);
    }

    @PostMapping("reset-password")
    public ResponseResult resetPassword(@RequestBody ResetPasswordDto resetPasswordDto){
        return userService.resetPassword(resetPasswordDto);
    }


    @GetMapping("confirm-reset-password")
    public ResponseResult confirmResetPassword(String code){
        return userService.confirmResetPassword(code);
    }

}
