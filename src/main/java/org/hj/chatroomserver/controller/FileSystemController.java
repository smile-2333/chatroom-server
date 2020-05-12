package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.model.result.ResponseResult;
import org.hj.chatroomserver.model.vo.UserFileVo;
import org.hj.chatroomserver.service.FileSystemService;
import org.hj.chatroomserver.util.UserState;
import org.springframework.data.domain.Page;
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
    public ResponseResult upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws Exception {
        return ResponseResult.SUCCESS(fileSystemService.upload(file));
    }

    @GetMapping("recent-files/{filesNum}")
    public Page<UserFileVo> recentFiles(@PathVariable("filesNum")int filesNum){
        return  fileSystemService.findRecentFiles(filesNum, UserState.getUser().getUserId());
    }
}
