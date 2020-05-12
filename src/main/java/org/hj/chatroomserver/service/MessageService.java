package org.hj.chatroomserver.service;

import org.hj.chatroomserver.model.dto.MessageDto;
import org.hj.chatroomserver.model.dto.QueryMessageDto;
import org.hj.chatroomserver.model.entity.Message;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.repository.MessageRepository;
import org.hj.chatroomserver.repository.UserRepository;
import org.hj.chatroomserver.util.BeanUtils;
import org.hj.chatroomserver.util.persistence.QueryHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public MessageVo saveMessage(MessageDto messageDto){
        final Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setContextType(messageDto.getContextType());
        message.setSenderId(messageDto.getUser().getUserId());
        message.setReceiverId(messageDto.getReceiverId());
        final Message save = messageRepository.save(message);

        final MessageVo messageVo = BeanUtils.copyProperties(save, MessageVo.class);
        final User user = messageDto.getUser();
        messageVo.setUsername(user.getUsername());
        messageVo.setAvatar(user.getAvatar());
        messageVo.setSenderId(user.getUserId());
        return messageVo;
    }

    public Page<MessageVo> query(QueryMessageDto queryMessageDto, Pageable pageable) {
        final Specification<Message> specification = QueryHelper.parse(queryMessageDto,Message.class);
        final Page<Message> raw = messageRepository.findAll(specification, pageable);
        return convert(raw);
    }

    private Page<MessageVo> convert(Page<Message> raw) {
        final Set<Integer> ownerIds = raw.getContent().stream().map(Message::getSenderId).collect(Collectors.toSet());
        final Map<Integer, User> ownerMap = userRepository.findByUserIdIn(ownerIds).stream().collect(Collectors.toMap(User::getUserId, Function.identity()));

        final Page<MessageVo> messageVos = raw.map(message -> {
            final MessageVo messageVo = BeanUtils.copyProperties(message, MessageVo.class);
            final User user = ownerMap.get(message.getSenderId());
            messageVo.setUsername(user.getUsername());
            messageVo.setAvatar(user.getAvatar());
            return messageVo;
        });

        final List<MessageVo> result = new ArrayList<>();
        final List<MessageVo> content = messageVos.getContent();
        for (int i=content.size()-1;i>-1;--i){
            result.add(content.get(i));
        }

        return new PageImpl<>(result,messageVos.getPageable(),messageVos.getTotalElements());
    }


}
