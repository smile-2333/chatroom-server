package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.result.FileUploadResponseResult;
import org.hj.chatroomserver.model.vo.UserFileVo;
import org.hj.chatroomserver.service.FileSystemService;
import org.hj.chatroomserver.util.UserState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("file")
public class FileSystemController {
    private final FileSystemService fileSystemService;

    public FileSystemController(FileSystemService fileSystemService) {
        this.fileSystemService = fileSystemService;
    }

    @PostMapping("/upload")
    public FileUploadResponseResult upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
        return fileSystemService.upload(file);
    }

    @GetMapping("recent")
    public Page<UserFileVo> recentFiles(@PageableDefault(sort = "createTime",direction = Sort.Direction.DESC)  Pageable pageable){
        return  fileSystemService.findRecentFiles(UserState.getUser().getUserId(),pageable);
    }
}
