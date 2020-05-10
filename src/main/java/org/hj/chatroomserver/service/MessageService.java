package org.hj.chatroomserver.service;

import org.hj.chatroomserver.model.entity.Message;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.repository.MessageRepository;
import org.hj.chatroomserver.util.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageVo saveMessage(Message message){
        final Message save = messageRepository.save(message);
        return BeanUtils.copyProperties(save,MessageVo.class);
    }
}
