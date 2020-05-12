package org.hj.chatroomserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.dto.MessageDto;
import org.hj.chatroomserver.model.enums.ContextType;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.service.MessageService;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class WebSocketTestController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    public WebSocketTestController(SimpMessagingTemplate simpMessagingTemplate, MessageService messageService, ObjectMapper objectMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }

    /**
     * 单对单
     * @param user
     * @throws Exception
     */
    @MessageMapping("/chat")
    public void messageHandling(Principal user) throws Exception {
        System.out.println();
//        String destination = "/topic/" + HtmlUtils.htmlEscape(requestMessage.getRoom());
//
//        String sender = HtmlUtils.htmlEscape(requestMessage.getSender());  //htmlEscape  转换为HTML转义字符表示
//        String type = HtmlUtils.htmlEscape(requestMessage.getType());
//        String content = HtmlUtils.htmlEscape(requestMessage.getContent());
//        ResponseMessage response = new ResponseMessage(sender, type, content);
//
//        messagingTemplate.convertAndSend(destination, response);
    }

    /**
     * 聊天室
     *
     * @return
     */
    @CrossOrigin
    @MessageMapping("chatRoom")
    @SendTo("/subscribe/chatRoom")
    public MessageVo messageHandlingAll(@Payload Message message, MessageDto messageDto)  {
        try {
            final Map<String,String> map = objectMapper.readValue((byte[]) message.getPayload(), Map.class);
            messageDto.setContextType(Enum.valueOf(ContextType.class,map.get("ContextType")));
        }catch (Exception exp){
            throw new CustomException(CommonCode.CONVERT_ERROR);
        }
        final MessageVo messageVo = messageService.saveMessage(messageDto);
        return messageVo;
    }
}
