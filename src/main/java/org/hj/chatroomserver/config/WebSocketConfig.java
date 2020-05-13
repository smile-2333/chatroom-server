package org.hj.chatroomserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
	    // 设置用户可订阅主题，换句话说，也是接口可以发送到的主题
        config.enableSimpleBroker("/subscribe/openChat","/subscribe/chatRoom","/subscribe/chat");

        // 发送过来的统一前缀
        config.setApplicationDestinationPrefixes("/send");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

		/*
		 * 路径"/websocket"被注册为STOMP端点，对外暴露，客户端通过该路径接入WebSocket服务
		 */
		registry.addEndpoint("/websocket").setAllowedOrigins("*").withSockJS();


	}
}