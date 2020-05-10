package org.hj.chatroomserver.service;

import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.dto.UserDto;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseResult register(User user) {
        final User save = userRepository.save(user);

        /**
         * 发邮件
         */

        return ResponseResult.SUCCESS();
    }

    public UserDto login(String usernameOrEmail) {
        return new UserDto();
    }

    public UserDto thirdLogin() {
        return new UserDto();
    }

    public ResponseResult update(User user) {
        final User old = findRawUser(user.getUserId());
        old.setDescription(user.getDescription());
        userRepository.save(old);
        return ResponseResult.SUCCESS();
    }

    private User findRawUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomException(CommonCode.FAIL);
        });
    }

    public ResponseResult updateAvatar(int userId, MultipartFile file) {
        final User old = findRawUser(userId);
        /**
         * 上传，然后更新
         */

        userRepository.save(old);
        return ResponseResult.SUCCESS();
    }



}
