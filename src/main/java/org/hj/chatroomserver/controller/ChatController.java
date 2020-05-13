package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.model.vo.PrivateChatVo;
import org.hj.chatroomserver.service.ChatService;
import org.hj.chatroomserver.util.UserState;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("private/other/{receiverId}")
    public ResponseResult deleteReceiverChat(@PathVariable("receiverId")int receiverId){
        return chatService.deleteReceiverChat(UserState.getUser().getUserId(),receiverId);
    }
}
