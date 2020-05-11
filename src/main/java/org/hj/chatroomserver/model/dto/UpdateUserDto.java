package org.hj.chatroomserver.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @see org.hj.chatroomserver.model.entity.User
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class UpdateUserDto {

    private Integer userId;

    private String username;

    private String description;

    private String city;

    private String phone;

    private String email;
}
