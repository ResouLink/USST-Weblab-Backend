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
public class FileList implements Serializable {
    /**
    * 附件关联oss表主键
    */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 附件表主键
     */
    private long fileId;
    /**
     * 附件角色，0代表是提出问题的附件，1代表回答问题的附件，2代表学习资源的附件，3代表头像数据
     */
    private long fileRole;

    /**
     * 归属于对应表里面的主键，由file_role决定去回答表还是去提问还是用户表表查询
     */
    private long nodeId;


}
