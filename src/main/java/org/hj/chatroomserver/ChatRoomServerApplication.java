package org.hj.chatroomserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChatRoomServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatRoomServerApplication.class, args);
    }

}
