package com.weblab.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class File implements Serializable {

    /**
     * 附件表主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 文件大小
     */
    private long fileSize;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件的url
     */
    private String fileUrl;

}
