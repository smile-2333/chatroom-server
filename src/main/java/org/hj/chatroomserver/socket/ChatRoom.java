package org.hj.chatroomserver.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.entity.Message;
import org.hj.chatroomserver.model.enums.ContextType;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.service.MessageService;
import org.hj.chatroomserver.util.UserState;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class ChatRoom extends TextWebSocketHandler {

    private final MessageService messageService;
    private final ObjectMapper objectMapper;

    public ChatRoom(MessageService messageService, ObjectMapper objectMapper) {
        this.messageService = messageService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        final Message messageEntity = new Message();
        messageEntity.setContent(message.getPayload());
        messageEntity.setOwnerId(UserState.getUser().getUserId());
        messageEntity.setContextType(ContextType.TEXT);
        final MessageVo messageVo = messageService.saveMessage(messageEntity);
        messageVo.setAvatar(UserState.getUser().getAvatar());
        messageVo.setUsername(UserState.getUser().getUsername());

        try {
            final String s = objectMapper.writeValueAsString(messageVo);
            session.sendMessage(new TextMessage(s));
        } catch (Exception ex) {
            throw new CustomException(CommonCode.MESSAGE_SENT_ERROR);
        }
    }

}