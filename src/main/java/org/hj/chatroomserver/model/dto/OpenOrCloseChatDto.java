package org.hj.chatroomserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hj.chatroomserver.model.entity.User;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenOrCloseChatDto {
    private User user;
    private Integer targetUserId;
}
