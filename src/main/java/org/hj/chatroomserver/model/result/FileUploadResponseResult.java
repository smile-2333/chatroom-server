package org.hj.chatroomserver.model.result;

import lombok.Data;
import org.hj.chatroomserver.model.enums.ResourceType;

@Data
public class FileUploadResponseResult{
    private boolean success;
    private int code;
    private String message;
    private String fileName;
    private ResourceType resourceType;
    private String filePath;


    public FileUploadResponseResult(ResultCode resultCode){
        this.success = resultCode.success();
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static FileUploadResponseResult SUCCESS(){
        return new FileUploadResponseResult(CommonCode.SUCCESS);
    }
}
