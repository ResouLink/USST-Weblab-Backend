package com.weblab.common.utils;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
public class AliOssUtil {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String upload(byte[] bytes, String objectName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, objectName,new ByteArrayInputStream(bytes));
        if(!ossClient.doesObjectExist(bucketName, objectName)){
            System.out.println("文件上传失败");
        }
        ossClient.shutdown();
        return "http://" +
                bucketName +
                "." +
                endpoint +
                "/" +
                objectName;
    }
    public Boolean delete(String objectName) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.deleteObject(bucketName, objectName);
        return !ossClient.doesObjectExist(bucketName, objectName);
    }
}
