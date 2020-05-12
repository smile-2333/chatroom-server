package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.vo.PrivateChatVo;
import org.hj.chatroomserver.service.ChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("chat")
public class ChatController {


    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("private/{senderId}")
    public List<PrivateChatVo> getPrivateChatList(@PathVariable("senderId")int senderId){
        return chatService.getPrivateChatList(senderId);
    }
}
