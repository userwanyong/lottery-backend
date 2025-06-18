package com.lottery.trigger.http;

import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import com.lottery.types.util.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author 永
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    /**
     * 文件上传
     * @param file 文件
     * @return url
     * @throws IOException io
     */
    @PostMapping("/upload")
    public BaseResponse<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传，文件名:{}",file.getOriginalFilename());
        //调用阿里云OSS工具类进行上传
        String url = aliOSSUtils.upload(file);
        log.info("文件上传成功，文件访问地址为：{}",url);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), url);
    }

}
