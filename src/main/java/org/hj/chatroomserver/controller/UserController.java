package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.dto.UpdateUserDto;
import org.hj.chatroomserver.model.dto.UserDto;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.service.UserService;
import org.hj.chatroomserver.util.UserState;
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


    @PostMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto user){
        user.setUserId(UserState.getUser().getUserId());
        return userService.updateUser(user);
    }

}
