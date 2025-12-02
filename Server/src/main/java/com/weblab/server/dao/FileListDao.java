package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.common.enums.RoleEnum;
import com.weblab.server.entity.FileList;
import com.weblab.server.mapper.FileListMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileListDao extends ServiceImpl<FileListMapper, FileList> {
    /**
     * 获取文件id
     * @param fileRole 文件角色
     * @param nodeId 节点id
     * @return
     */
    public List<Long> getFileIds(FileRoleEnum fileRole, Long nodeId) {
        return baseMapper.getFileIds(fileRole, nodeId);
    }



    public String getAvatarUrl(Long nodeId) {
        return baseMapper.selectAvatarUrlByNodeId(nodeId);
    }
}
