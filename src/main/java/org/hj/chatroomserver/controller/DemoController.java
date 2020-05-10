package org.hj.chatroomserver.controller;

import org.hj.chatroomserver.exception.CustomException;
import org.hj.chatroomserver.model.result.CommonCode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class DemoController {
    @GetMapping("/demo")
    @PreAuthorize("hasAuthority('PERMISSION_DEMO')")
    public String demo() {
        return "demo";
    }


    @GetMapping("/demo2")
    @PreAuthorize("hasAuthority('PERMISSION_DEMO2')")
    public String demo2() {
        return "demo";
    }

    @GetMapping("/demo_without_auth")
    public String demoWithoutAuth() {
        return "demo without auth";
    }

    @GetMapping("/exceptionDemo")
    public String exceptionDemo() throws Exception {
        throw new Exception();
    }

    @GetMapping("/custom_exception_demo")
    public String customExceptionDemo() throws Exception {
        throw new CustomException(CommonCode.FAIL);
    }

    @GetMapping("/avatar_demo")
    public String execute(HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) throws IOException {
        // img为图片的二进制流
//        String templatePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"template/";
        File avatar = new File("C:\\Users\\smile2333\\IdeaProjects\\qa_server\\src\\main\\resources\\static\\avatar.png");
        FileInputStream inputStream = new FileInputStream(avatar);

        byte [] img = new byte[(int)avatar.length()];

        inputStream.read(img);
        httpServletResponse.setContentType("image/png");
        OutputStream os = httpServletResponse.getOutputStream();
        os.write(img);
        os.flush();
        os.close();
        return "success";
    }


    @GetMapping("/avatar_demo2")
    public String execute2(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse) throws IOException {
        // img为图片的二进制流
//        String templatePath = Thread.currentThread().getContextClassLoader().getResource("").getPath()+"template/";
        File avatar = new File("C:\\Users\\smile2333\\IdeaProjects\\qa_server\\src\\main\\resources\\static\\avatar2.jpg");
        FileInputStream inputStream = new FileInputStream(avatar);

        byte [] img = new byte[(int)avatar.length()];

        inputStream.read(img);
        httpServletResponse.setContentType("image/jpg");
        OutputStream os = httpServletResponse.getOutputStream();
        os.write(img);
        os.flush();
        os.close();
        return "success";
    }

}
