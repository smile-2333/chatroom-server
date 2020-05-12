package org.hj.chatroomserver.service;

import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.dto.OpenOrCloseChatDto;
import org.hj.chatroomserver.model.entity.PrivateChat;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.vo.PrivateChatVo;
import org.hj.chatroomserver.repository.PrivateChatRepository;
import org.hj.chatroomserver.repository.UserRepository;
import org.hj.chatroomserver.util.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final PrivateChatRepository privateChatRepository;
    private final UserRepository userRepository;

    public ChatService(PrivateChatRepository privateChatRepository, UserRepository userRepository) {
        this.privateChatRepository = privateChatRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean openChat(OpenOrCloseChatDto openOrCloseChatDto) {
        final PrivateChat senderSideChat = new PrivateChat();
        senderSideChat.setSenderId(openOrCloseChatDto.getUser().getUserId());
        senderSideChat.setReceiverId(openOrCloseChatDto.getTargetUserId());

        final PrivateChat receiverSideChat = new PrivateChat();
        receiverSideChat.setSenderId(openOrCloseChatDto.getTargetUserId());
        receiverSideChat.setReceiverId(openOrCloseChatDto.getUser().getUserId());

        privateChatRepository.save(senderSideChat);
        privateChatRepository.save(receiverSideChat);

        return true;
    }

    public List<PrivateChatVo> getPrivateChatList(int senderId) {
        final List<PrivateChat> raw = privateChatRepository.findBySenderId(senderId);
        if (raw.isEmpty()){
            return Collections.emptyList();
        }
        return convert(raw);
    }

    private List<PrivateChatVo> convert(List<PrivateChat> raw) {
        /**
         * 接收者
         */
        final Set<Integer> receiverIds = raw.stream().map(PrivateChat::getReceiverId).collect(Collectors.toSet());
        final Map<Integer, User> receiverMap = userRepository.findByUserIdIn(receiverIds).stream().collect(Collectors.toMap(User::getUserId, Function.identity()));

        /**
         * 发送者
         */
        final User sender = userRepository.findById(raw.get(0).getSenderId()).orElseThrow(() -> new CustomException(CommonCode.ACCOUNT_NOT_EXIST));

        return raw.stream().map(privateChat -> {
            final PrivateChatVo privateChatVo = BeanUtils.copyProperties(privateChat, PrivateChatVo.class);
            privateChatVo.setSender(sender);
            privateChatVo.setReceiver(receiverMap.get(privateChat.getReceiverId()));
            return privateChatVo;
        }).collect(Collectors.toList());
    }

    public boolean closeChat(OpenOrCloseChatDto openOrCloseChatDto) {
        final PrivateChat senderChat = privateChatRepository.findBySenderIdAndReceiverId(openOrCloseChatDto.getUser().getUserId(), openOrCloseChatDto.getTargetUserId()).orElseThrow(() -> new CustomException(CommonCode.PRIVATE_CHAT_NOT_EXIST));
        final PrivateChat receiverChat = privateChatRepository.findBySenderIdAndReceiverId(openOrCloseChatDto.getTargetUserId(),openOrCloseChatDto.getUser().getUserId()).orElseThrow(() -> new CustomException(CommonCode.PRIVATE_CHAT_NOT_EXIST));
        privateChatRepository.delete(senderChat);
        privateChatRepository.delete(receiverChat);
        return true;
    }
}
