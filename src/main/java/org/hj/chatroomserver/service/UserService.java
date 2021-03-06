package org.hj.chatroomserver.service;

import org.apache.commons.lang3.StringUtils;
import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.dto.QueryUserDto;
import org.hj.chatroomserver.model.dto.ResetPasswordDto;
import org.hj.chatroomserver.model.dto.UpdateUserDto;
import org.hj.chatroomserver.model.dto.UserDto;
import org.hj.chatroomserver.model.entity.User;
import org.hj.chatroomserver.model.enums.Role;
import org.hj.chatroomserver.model.enums.Status;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.model.vo.OtherUserVo;
import org.hj.chatroomserver.repository.UserRepository;
import org.hj.chatroomserver.util.BeanUtils;
import org.hj.chatroomserver.util.UserState;
import org.hj.chatroomserver.util.persistence.QueryHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FileSystemService fileSystemService;

    public UserService(UserRepository userRepository, EmailService emailService, FileSystemService fileSystemService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.fileSystemService = fileSystemService;
        UserState.setUserService(this);
    }

    public ResponseResult register(User user) {

        user.setRole(Role.NORMAL);
        user.setAvatar("http://47.93.53.45/M00/00/00/L101LV5zszKAavNcAAK1VqIpjXE762.png");
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setDescription("我是新来的，请多多关照");
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

        user.setLastLoginTime(new Date());
        userRepository.save(user);
        userDto.setLastLoginTime(user.getLastLoginTime());
        return userDto;
    }

    public UserDto login(String usernameOrEmail,String password) {
        final UserDto login = this.login(usernameOrEmail);
        if (login !=null){

            if (new BCryptPasswordEncoder().matches(password,login.getPassword())){
                return login;
            }
        }
        return null;
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
        String filePath = fileSystemService.upload(file).getFilePath();
        old.setAvatar(filePath);

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
            user.setCity(userDto.getCity());
        }

        if (!StringUtils.equals(user.getPhone(), userDto.getPhone())) {
            user.setDescription(userDto.getPhone());
        }

        userRepository.save(user);
        UserState.refreshUser();
        return ResponseResult.SUCCESS();
    }

    public void logout(int userId){
        final User user = findRawUser(userId);
        user.setLastLogoutTime(new Date());
        userRepository.save(user);
    }

    public OtherUserVo findOtherByUserId(int userId) {
        final User rawUser = findRawUser(userId);
        return BeanUtils.copyProperties(rawUser,OtherUserVo.class);
    }

    public ResponseResult resetPassword(ResetPasswordDto resetPasswordDto) {
        final User user = userRepository.findByEmailOrUsername(resetPasswordDto.getUsernameOrEmail(), resetPasswordDto.getUsernameOrEmail()).orElseThrow(() -> new CustomException(CommonCode.ACCOUNT_NOT_EXIST));
        user.setPassword(resetPasswordDto.getNewPassword());
        emailService.sendResetPasswordEmail(user);
        return ResponseResult.SUCCESS();
    }

    public ResponseResult confirmResetPassword(String code) {
        final User user = emailService.confirmSendResetPasswordEmail(code);

        final User rawUser = findRawUser(user.getUserId());
        rawUser.setPassword(new  BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(rawUser);

        return ResponseResult.SUCCESS();
    }

    public Page<OtherUserVo> getUsers(QueryUserDto queryUserDto, Pageable pageable) {
        final Specification<User> specification = QueryHelper.parse(queryUserDto, User.class);
        return userRepository.findAll(specification,pageable).map(
                user -> {
                    final OtherUserVo otherUserVo = BeanUtils.copyProperties(user, OtherUserVo.class);
                    otherUserVo.setStatusDescription(Status.getStatus(user.getIsFreeze()).getDescription());
                    return otherUserVo;
                }
        );
    }

    public ResponseResult freezeUser(int userId) {
        final User user = findRawUser(userId);
        if (user.getIsFreeze()==false){
            user.setIsFreeze(true);
            userRepository.save(user);
        }
        return ResponseResult.SUCCESS();
    }

    public ResponseResult deleteUser(int userId) {
        final User user = findRawUser(userId);
        userRepository.delete(user);
        return ResponseResult.SUCCESS();
    }

    public ResponseResult deleteUser(int[] userIds) {
        for (int userId : userIds) {
            deleteUser(userId);
        }
        return ResponseResult.SUCCESS();
    }

}
