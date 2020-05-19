package org.hj.chatroomserver.service;

import org.hj.chatroomserver.model.dto.MessageDto;
import org.hj.chatroomserver.model.dto.QueryMessageDto;
import org.hj.chatroomserver.model.entity.Message;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.repository.MessageRepository;
import org.hj.chatroomserver.repository.UserRepository;
import org.hj.chatroomserver.util.BeanUtils;
import org.hj.chatroomserver.util.persistence.QueryHelper;
import org.hj.chatroomserver.util.persistence.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;
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

    public MessageVo saveMessage(MessageDto messageDto) {
        final Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setContextType(messageDto.getContextType());
        message.setSenderId(messageDto.getUser().getUserId());
        message.setReceiverId(messageDto.getReceiverId());
        message.setResourceType(messageDto.getResourceType());
        message.setFileName(messageDto.getFileName());
        final Message save = messageRepository.save(message);

        final MessageVo messageVo = BeanUtils.copyProperties(save, MessageVo.class);
        final User user = messageDto.getUser();
        messageVo.setUsername(user.getUsername());
        messageVo.setAvatar(user.getAvatar());
        messageVo.setSenderId(user.getUserId());
        return messageVo;
    }

    public Page<MessageVo> query(QueryMessageDto queryMessageDto, Pageable pageable) {
        Specification<Message> specification = initSpecification(queryMessageDto);
        final Page<Message> raw = messageRepository.findAll(specification, pageable);
        return convert(raw);
    }


    public Page<MessageVo> adminQuery(QueryMessageDto queryMessageDto, Pageable pageable) {
        Specification<Message> specification = initSpecification(queryMessageDto);
        final Page<Message> raw = messageRepository.findAll(specification, pageable);
        return convertWithoutReverse(raw);
    }


    private Specification<Message> initSpecification(QueryMessageDto queryMessageDto) {

        final SpecificationBuilder<Message> messageSpecificationBuilder = QueryHelper.parseBuilder(queryMessageDto, Message.class);

        return ((root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (queryMessageDto.getSenderIdOrReceiverId() != null && queryMessageDto.getReceiverIdOrSenderId() != null) {

                if (queryMessageDto.getReceiverIdOrSenderId() == -1) {
                    predicates.add(criteriaBuilder.equal(root.get("receiverId").as(Integer.class), -1));
                } else {
                    final Predicate receiverId = criteriaBuilder.and(criteriaBuilder.equal(root.get("receiverId").as(Integer.class), queryMessageDto.getReceiverIdOrSenderId()));
                    final Predicate senderId = criteriaBuilder.and(criteriaBuilder.equal(root.get("senderId").as(Integer.class), queryMessageDto.getSenderIdOrReceiverId()));
                    final Predicate first = criteriaBuilder.and(receiverId, senderId);

                    final Predicate reverseReceiverId = criteriaBuilder.and(criteriaBuilder.equal(root.get("senderId").as(Integer.class), queryMessageDto.getReceiverIdOrSenderId()));
                    final Predicate reverseSenderId = criteriaBuilder.and(criteriaBuilder.equal(root.get("receiverId").as(Integer.class), queryMessageDto.getSenderIdOrReceiverId()));
                    final Predicate second = criteriaBuilder.and(reverseReceiverId, reverseSenderId);

                    predicates.add(criteriaBuilder.or(first, second));
                }

            }

            if (queryMessageDto.getFileNames()!=null&&queryMessageDto.getFileNames().size()>0){
                Predicate inLikePredicates = null;
                for (String fileName : queryMessageDto.getFileNames()) {
                    if (inLikePredicates == null) {
                       inLikePredicates =  criteriaBuilder.like(root.get("fileName").as(String.class),"%"+fileName+"%");
                    }else {
                       inLikePredicates = criteriaBuilder.or(inLikePredicates,criteriaBuilder.like(root.get("fileName").as(String.class),"%"+fileName+"%"));
                    }
                }
                predicates.add(inLikePredicates);
            }

            if (queryMessageDto.getContents()!=null&&queryMessageDto.getContents().size()>0){
                Predicate inLikePredicates = null;
                for (String content : queryMessageDto.getContents()) {
                    if (inLikePredicates == null) {
                        inLikePredicates =  criteriaBuilder.like(root.get("content").as(String.class),"%"+content+"%");
                    }else {
                        inLikePredicates = criteriaBuilder.or(inLikePredicates,criteriaBuilder.like(root.get("content").as(String.class),"%"+content+"%"));
                    }
                }
                predicates.add(inLikePredicates);
            }

            predicates.add(messageSpecificationBuilder.build().toPredicate(root, query, criteriaBuilder));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private Page<MessageVo> convert(Page<Message> raw) {
        final Page<MessageVo> messageVos = convertWithoutReverse(raw);

        final List<MessageVo> result = new ArrayList<>();
        final List<MessageVo> content = messageVos.getContent();
        for (int i = content.size() - 1; i > -1; --i) {
            result.add(content.get(i));
        }

        return new PageImpl<>(result, messageVos.getPageable(), messageVos.getTotalElements());
    }

    private Page<MessageVo> convertWithoutReverse(Page<Message> raw) {
        /**
         * 新的替换
         */
        final Set<Integer> senderIds = raw.getContent().stream().map(Message::getSenderId).collect(Collectors.toSet());
        final Set<Integer> receiverIds = raw.getContent().stream().map(Message::getReceiverId).collect(Collectors.toSet());
        final HashSet<Integer> ids = new HashSet<>();
        ids.addAll(senderIds);
        ids.addAll(receiverIds);
        final Map<Integer, User> userMap = userRepository.findByUserIdIn(ids).stream().collect(Collectors.toMap(User::getUserId, Function.identity()));


        return raw.map(message -> {
            final MessageVo messageVo = BeanUtils.copyProperties(message, MessageVo.class);
            final User user = userMap.get(message.getSenderId());
            /**
             * 待废弃
             */
            messageVo.setUsername(user.getUsername());
            messageVo.setAvatar(user.getAvatar());

            messageVo.setSender(userMap.get(message.getSenderId()));
            messageVo.setReceiver(userMap.get(message.getReceiverId()));

            return messageVo;
        });
    }


    public ResponseResult deleteMessage(int messageId) {
        messageRepository.deleteById(messageId);
        return ResponseResult.SUCCESS();
    }

    public ResponseResult deleteMessages(int[] messageIds) {
        for (int messageId : messageIds) {
            deleteMessage(messageId);
        }

        return ResponseResult.SUCCESS();
    }
}
