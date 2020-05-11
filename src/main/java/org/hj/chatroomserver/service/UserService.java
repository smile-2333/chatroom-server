package org.hj.chatroomserver.service;

import org.apache.commons.lang3.StringUtils;
import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.dto.UpdateUserDto;
import org.hj.chatroomserver.model.dto.UserDto;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.enums.Role;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.repository.UserRepository;
import org.hj.chatroomserver.util.BeanUtils;
import org.hj.chatroomserver.util.FastdfsHelper;
import org.hj.chatroomserver.util.UserState;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FastdfsHelper fastdfsHelper;

    public UserService(UserRepository userRepository, EmailService emailService, FastdfsHelper fastdfsHelper) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.fastdfsHelper = fastdfsHelper;
        UserState.setUserService(this);
    }

    public ResponseResult register(User user) {

        user.setRole(Role.NORMAL);
        user.setAvatar("http://47.93.53.45/M00/00/00/L101LV5zszKAavNcAAK1VqIpjXE762.png");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        final User save = userRepository.save(user);

        /**
         * 发邮件
         */
        emailService.sendActivationEmail(user);

        return ResponseResult.SUCCESS();
    }

    public UserDto login(String usernameOrEmail) {
        User user = preCheck(usernameOrEmail);

        final UserDto userDto = BeanUtils.copyProperties(user, UserDto.class);
        userDto.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRole().toString()));

        return userDto;
    }

    private User preCheck(String usernameOrEmail) {
        final Optional<User> byUsername = userRepository.findByUsername(usernameOrEmail);
        final Optional<User> byEmail = userRepository.findByEmail(usernameOrEmail);

        User user = null;
        if (byUsername.isPresent()) {
            user = byUsername.get();
        } else if (byEmail.isPresent()) {
            user = byEmail.get();
        } else {
            throw new CustomException(CommonCode.INVALID_USERNAME_OR_PASSWORD);
        }

        if (user.getIsActive() == false) {
            throw new CustomException(CommonCode.ACCOUNT_NOT_ACTIVE);
        }
        return user;
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

    public User findRawUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomException(CommonCode.USER_NOT_FIND);
        });
    }

    @Transactional
    public ResponseResult updateAvatar(int userId, MultipartFile file) {
        final User old = findRawUser(userId);

        /**
         * 上传，然后更新
         */
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

        try {
            final String filePath = fastdfsHelper.upload(file.getInputStream(), extName);
            old.setAvatar(filePath);
        } catch (Exception exp) {
            throw new CustomException(CommonCode.UPLOAD_IMAGE_FAIL);
        }

        userRepository.save(old);
        UserState.refreshUser();

        return ResponseResult.SUCCESS();
    }

    public ResponseResult activate(String code) {

        final int userId = emailService.confirmActivationEmail(code);
        final User user = findRawUser(userId);
        if (!user.getIsActive()) {
            user.setIsActive(true);
            userRepository.save(user);
        }

        return new ResponseResult(CommonCode.ACTIVATE_SUCCESS);
    }

    public ResponseResult updateUser(UpdateUserDto userDto) {
        final User user = findRawUser(userDto.getUserId());


        if (!StringUtils.equals(user.getUsername(), userDto.getUsername())) {
            userRepository.findByUsername(userDto.getUsername()).ifPresent(existUser -> {
                throw new CustomException(CommonCode.USERNAME_BEEN_USED);
            });
            user.setUsername(userDto.getUsername());
        }

        if (!StringUtils.equals(user.getEmail(), userDto.getEmail())) {
            userRepository.findByEmail(userDto.getEmail()).ifPresent(existUser -> {
                throw new CustomException(CommonCode.EMAIL_BEEN_USED);
            });
            user.setEmail(userDto.getEmail());
        }

        if (!StringUtils.equals(user.getDescription(), userDto.getDescription())) {
            user.setDescription(userDto.getDescription());
        }

        if (!StringUtils.equals(user.getCity(), userDto.getCity())) {
            user.setDescription(userDto.getCity());
        }

        if (!StringUtils.equals(user.getPhone(), userDto.getPhone())) {
            user.setDescription(userDto.getPhone());
        }

        userRepository.save(user);
        UserState.refreshUser();
        return ResponseResult.SUCCESS();
    }
}
