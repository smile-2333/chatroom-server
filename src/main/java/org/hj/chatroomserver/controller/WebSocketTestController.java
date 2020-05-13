package org.hj.chatroomserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.dto.MessageDto;
import org.hj.chatroomserver.model.dto.OpenOrCloseChatDto;
import org.hj.chatroomserver.model.enums.ContextType;
import org.hj.chatroomserver.model.enums.Event;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.service.ChatService;
import org.hj.chatroomserver.service.MessageService;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class WebSocketTestController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    public WebSocketTestController(SimpMessagingTemplate simpMessagingTemplate, MessageService messageService, ObjectMapper objectMapper, ChatService chatService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
        this.objectMapper = objectMapper;
        this.chatService = chatService;
    }

    /**
     * 私聊
     * @throws Exception
     */
    @MessageMapping("/chat")
    public void messageHandling(@Payload Message message,MessageDto messageDto) throws Exception {
        final Map<String,String> map = objectMapper.readValue((byte[]) message.getPayload(), Map.class);
        messageDto.setContextType(Enum.valueOf(ContextType.class,map.get("ContextType")));
        final MessageVo sentMessage = messageService.saveMessage(messageDto);
        simpMessagingTemplate.convertAndSend(String.format("/subscribe/chat/sender/%s/receiver/%s",messageDto.getUser().getUserId(),messageDto.getReceiverId()),sentMessage);
    }

    /**
     * 群聊
     *
     * @return
     */
    @MessageMapping("chatRoom")
    @SendTo("/subscribe/chatRoom")
    public MessageVo messageHandlingAll(@Payload Message message, MessageDto messageDto)  {
        try {
            final Map<String,String> map = objectMapper.readValue((byte[]) message.getPayload(), Map.class);

            Event event = Enum.valueOf(Event.class,map.get("event"));

            switch (event){
                case LOGIN:
                    final MessageVo loginMessage = new MessageVo();
                    loginMessage.setContent(String.format("欢迎%s加入聊天室",messageDto.getUser().getUsername()));
                    loginMessage.setContextType(ContextType.WELCOME);
                    return loginMessage;
                case LOGOUT:
                    final MessageVo logoutMessage = new MessageVo();
                    logoutMessage.setContent(String.format("%s离开了聊天室",messageDto.getUser().getUsername()));
                    logoutMessage.setContextType(ContextType.WELCOME);
                    return logoutMessage;
                case SENT_MESSAGE:
                    messageDto.setContextType(Enum.valueOf(ContextType.class,map.get("ContextType")));
                    final MessageVo sentMessage = messageService.saveMessage(messageDto);
                    return sentMessage;

            }
        }catch (Exception exp){
            throw new CustomException(CommonCode.CONVERT_ERROR);
        }
        throw new CustomException(CommonCode.HANDLE_MESSAGE_ERROR);
    }

    /**
     * 打开私聊
     */
    @MessageMapping("openChat")
    public void openChat(OpenOrCloseChatDto openOrCloseChatDto){
        if (!chatService.openChat(openOrCloseChatDto)){
            throw new CustomException(CommonCode.PRIVATE_CHAT_ESTABLISH_FAIL);
        }

        simpMessagingTemplate.convertAndSend(String.format("/subscribe/openChat/%s", openOrCloseChatDto.getTargetUserId()), openOrCloseChatDto.getUser());
    }
}
