package org.hj.chatroomserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.enums.ContextType;

/**
 * @see org.hj.chatroomserver.model.entity.Message
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageDto {
    private User user;
    private String content;
    private ContextType contextType;
}

