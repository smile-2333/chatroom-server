package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.dto.QueryMessageDto;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.service.MessageService;
import org.hj.chatroomserver.util.UserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public Page<MessageVo> applyMoreMessage(QueryMessageDto queryMessageDto,@PageableDefault(sort = "createTime",direction = Sort.Direction.DESC) Pageable pageable){
        queryMessageDto.setEndTime(UserState.getUser().getLastLoginTime());
        return messageService.query(queryMessageDto,pageable);
    }

}
