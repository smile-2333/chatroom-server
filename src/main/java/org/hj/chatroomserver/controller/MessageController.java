package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.dto.QueryMessageDto;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.model.vo.MessageVo;
import org.hj.chatroomserver.service.MessageService;
import org.hj.chatroomserver.util.UserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PreAuthorize("hasAnyAuthority('NORMAL')")
    @GetMapping
    public Page<MessageVo> query(QueryMessageDto queryMessageDto,@PageableDefault(sort = "createTime",direction = Sort.Direction.DESC) Pageable pageable){
        queryMessageDto.setCreateTimeEnd(UserState.getUser().getLastLoginTime());
        return messageService.query(queryMessageDto,pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("admin")
    public Page<MessageVo> adminQuery(QueryMessageDto queryMessageDto,@PageableDefault(sort = "createTime",direction = Sort.Direction.DESC) Pageable pageable){
        return messageService.adminQuery(queryMessageDto,pageable);
    }


    @PreAuthorize("hasAnyAuthority('NORMAL','ADMIN')")
    @DeleteMapping("{messageId}")
    public ResponseResult deleteMessage(@PathVariable("messageId")int messageId){
        return messageService.deleteMessage(messageId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("batch")
    public ResponseResult batchDelete(int[] messageIds){
        return messageService.deleteMessages(messageIds);
    }

}
