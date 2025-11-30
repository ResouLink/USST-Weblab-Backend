package com.weblab.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weblab.common.core.domain.FileRole;
import com.weblab.server.entity.FileList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileListMapper extends BaseMapper<FileList> {
    /**
     * 获取文件id
     * @param fileRole 文件角色
     * @param nodeId 节点id
     * @return
     */
    List<Long> getFileIds(FileRole fileRole, Long nodeId);
}
