package org.hj.chatroomserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hj.chatroomserver.config.mq.RabbitmqConfig;
import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.util.CacheHelper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final CacheHelper cacheHelper;

    @Value("${hostIP}")
    private String hostIP;

    public EmailService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper, CacheHelper cacheHelper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.cacheHelper = cacheHelper;
    }

    public void sendActivationEmail(User user)  {
        UUID key = UUID.randomUUID();

        String activateURL = String.format("%s%s:9090%s?code=%s","http://",hostIP,"/user/activate", key);
        String content = "Please click the url to activate: " + activateURL;
        String subject = "Activate your account";
        Map<String,String> mailMap = new HashMap<>();
        mailMap.put("content",content);
        mailMap.put("subject",subject);
        mailMap.put("from","980959100@qq.com");
        mailMap.put("to",user.getEmail());

        try {
            final String infoStr = objectMapper.writeValueAsString(mailMap);
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_EMAIL,RabbitmqConfig.KEY,infoStr);
            cacheHelper.putSignUpCache(key.toString(),user.getUserId());
        }catch (JsonProcessingException ex){
            throw new CustomException(CommonCode.SEND_EMAIL_FAIL);
        }
    }


    public int confirmActivationEmail(String code)  {
        final Integer userId = cacheHelper.getFromSignUpCache(code);

        if (userId == null) {
            throw new CustomException(CommonCode.MAIL_EXPIRED);
        }

        return userId;
    }


    public void sendResetPasswordEmail(User user)  {
        UUID key = UUID.randomUUID();

        String activateURL = String.format("%s%s:9090%s?code=%s","http://",hostIP,"/user/confirm-reset-password", key);
        String content = "Please click the url to reset password: " + activateURL;
        String subject = "Activate your account";
        Map<String,String> mailMap = new HashMap<>();
        mailMap.put("content",content);
        mailMap.put("subject",subject);
        mailMap.put("from","980959100@qq.com");
        mailMap.put("to",user.getEmail());

        try {
            final String infoStr = objectMapper.writeValueAsString(mailMap);
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_EMAIL,RabbitmqConfig.KEY,infoStr);
            cacheHelper.getResetPassword().put(key.toString(),user);
        }catch (JsonProcessingException ex){
            throw new CustomException(CommonCode.SEND_EMAIL_FAIL);
        }
    }

    public User confirmSendResetPasswordEmail(String code){

       return cacheHelper.getResetPassword().get(code);
    }
}
