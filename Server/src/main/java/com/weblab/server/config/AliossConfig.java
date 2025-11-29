package com.weblab.server.config;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.weblab.common.utils.AliOssUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "alioss")
@Data
public class AliossConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    @Bean
    public AliOssUtil ossClient() {
        return  new AliOssUtil(endpoint, accessKeyId, accessKeySecret, bucketName);
    }
}
