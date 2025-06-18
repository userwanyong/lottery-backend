package com.lottery.types.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云OSS工具类
 * @author 永
 */
@Component
public class AliOSSUtils {

    @Value("${alioss.endpoint}")
    private String endpoint;
    @Value("${alioss.accessKeyId}")
    private String accessKeyId;
    @Value("${alioss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${alioss.bucketName}")
    private String bucketName;

    /**
     * 上传图片到OSS
     */
    public String upload(MultipartFile file) throws IOException {
        //获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();
        //避免文件覆盖
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
        //上传文件到OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);
        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        //关闭OSSClient
        ossClient.shutdown();
        //返回OSS上的路径
        return url;
    }


}
