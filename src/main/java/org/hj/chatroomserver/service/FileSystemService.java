package org.hj.chatroomserver.service;

import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.result.CommonCode;
import org.hj.chatroomserver.model.vo.UserFileVo;
import org.hj.chatroomserver.util.FastdfsHelper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemService {
    private final FastdfsHelper fastdfsHelper;

    public FileSystemService(FastdfsHelper fastdfsHelper) {
        this.fastdfsHelper = fastdfsHelper;
    }


    public String upload(MultipartFile file) {
        try {
            String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            final String filePath = fastdfsHelper.upload(file.getInputStream(), extName);
            return filePath;
        } catch (Exception exp) {
            throw new CustomException(CommonCode.UPLOAD_IMAGE_FAIL);
        }
    }

    public Page<UserFileVo> findRecentFiles(int filesNum, Integer userId) {
        return null;
    }
}
