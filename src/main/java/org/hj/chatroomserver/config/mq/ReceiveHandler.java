package org.hj.chatroomserver.config.mq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReceiveHandler {
    private final JavaMailSender javaMailSender;
    private final ObjectMapper jsonMapper;

    public ReceiveHandler(JavaMailSender javaMailSender, ObjectMapper jsonMapper) {
        this.javaMailSender = javaMailSender;
        this.jsonMapper = jsonMapper;
    }

    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void sendEmail(String msg, Message message, Channel channel) throws JsonProcessingException {
        Map<String, String> map = jsonMapper.readValue(msg, Map.class);
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setFrom(map.get("from"));
        mail.setTo(map.get("to"));
        mail.setSubject(map.get("subject"));
        mail.setText(map.get("content"));
        javaMailSender.send(mail);
    }
}
