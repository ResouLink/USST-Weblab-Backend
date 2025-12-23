package com.weblab.server.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileVO implements java.io.Serializable {
    /**
     * 文件id，对应附件表的主键
     */
    private long fileId;
    /**
     * 上传文件的名称，是原本上传文件的名称，不是oss内的名称
     */
    private String fileName;
    /**
     * 文件大小，单位字节
     */
    private long fileSize;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件url
     */
    private String fileUrl;
}
