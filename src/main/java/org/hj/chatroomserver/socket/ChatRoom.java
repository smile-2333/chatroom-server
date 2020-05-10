package org.hj.chatroomserver.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hj.chatroomserver.model.entity.Message;
import org.hj.chatroomserver.model.enums.ContextType;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.service.MessageService;
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

		Message newMessage = new Message();
		newMessage.setContent(message.getPayload());
		newMessage.setAllWithCurrentTime();
		newMessage.setOwnerId(1);
		newMessage.setContextType(ContextType.TEXT);
		final MessageVo messageVo = messageService.saveMessage(newMessage);
		messageVo.setAvatar("http://roba.laborasyon.com/demos/dark/dist/media/img/avatar3.png");
		messageVo.setUsername(" Mirabelle Tow");

		final String s = objectMapper.writeValueAsString(messageVo);

		session.sendMessage(new TextMessage(s));
	}

}