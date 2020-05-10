package org.hj.chatroomserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hj.chatroomserver.service.MessageService;
import org.hj.chatroomserver.socket.ChatRoom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private final MessageService messageService;
	private final ObjectMapper objectMapper;

	public WebSocketConfig(MessageService messageService, ObjectMapper objectMapper) {
		this.messageService = messageService;
		this.objectMapper = objectMapper;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new ChatRoom(messageService, objectMapper), "/myHandler").setAllowedOrigins("http://localhost:8080");;

	}

	@Bean
	public WebSocketHandler myChatRoom() {
		return new ChatRoom(messageService, objectMapper);
	}

}