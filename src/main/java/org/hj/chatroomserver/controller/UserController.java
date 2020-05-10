package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }


}
