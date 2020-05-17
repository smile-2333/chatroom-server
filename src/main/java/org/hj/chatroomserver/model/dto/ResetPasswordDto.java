package org.hj.chatroomserver.model.dto;

import lombok.Data;

/**
 * @see org.hj.chatroomserver.model.entity.User
 */
@Data
public class ResetPasswordDto {
    private String usernameOrEmail;

    private String newPassword;
}
