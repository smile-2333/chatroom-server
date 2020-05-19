package org.hj.chatroomserver.service;

import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.entity.Message;
import org.hj.chatroomserver.model.enums.ContextType;
import org.hj.chatroomserver.model.enums.ResourceType;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.result.FileUploadResponseResult;
import org.hj.chatroomserver.model.vo.UserFileVo;
import org.hj.chatroomserver.repository.MessageRepository;
import org.hj.chatroomserver.util.BeanUtils;
import org.hj.chatroomserver.util.FastdfsHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemService {
    private final FastdfsHelper fastdfsHelper;
    private final MessageRepository messageRepository;

    public FileSystemService(FastdfsHelper fastdfsHelper, MessageRepository messageRepository) {
        this.fastdfsHelper = fastdfsHelper;
        this.messageRepository = messageRepository;
    }

    public FileUploadResponseResult upload(MultipartFile file) {
        try {
            String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            final String filePath = fastdfsHelper.upload(file.getInputStream(), extName);
            final FileUploadResponseResult result = FileUploadResponseResult.SUCCESS();
            result.setFileName(file.getOriginalFilename());
            result.setResourceType(file.getContentType().split("/")[0].equals("image")?ResourceType.IMAGE:ResourceType.OTHER);
            result.setFilePath(filePath);
            return result;
        } catch (Exception exp) {
            throw new CustomException(CommonCode.UPLOAD_IMAGE_FAIL);
        }
    }

    public Page<UserFileVo> findRecentFiles(Integer userId, Pageable pageable) {
        final Page<Message> messagesFiles = messageRepository.findBySenderIdAndContextType(userId, ContextType.RESOURCE,pageable);
        return convert(messagesFiles);
    }

    private Page<UserFileVo> convert(Page<Message> messagesFiles) {
        return messagesFiles.map(message -> BeanUtils.copyProperties(message,UserFileVo.class));
    }
}
