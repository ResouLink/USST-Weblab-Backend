package com.weblab.server.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.common.enums.RoleEnum;
import com.weblab.server.entity.FileList;
import com.weblab.server.mapper.FileListMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class FileListDao extends ServiceImpl<FileListMapper, FileList> {
    /**
     * 获取文件id
     * @param fileRole 文件角色
     * @param nodeId 节点id
     * @return
     */
    public List<Long> getFileIds(FileRoleEnum fileRole, Long nodeId) {

        return lambdaQuery()
                .eq(FileList::getFileRole, fileRole.getFileRole())
                .eq(FileList::getNodeId, nodeId)
                .list()
                .stream()
                .map(FileList::getFileId)
                .toList();
    }



    public String getAvatarUrl(Long nodeId) {
        return baseMapper.selectAvatarUrlByNodeId(nodeId);
    }

    public void addResourceFileList(List<Long> files,Long resourceId) {
        List<FileList> fileLists = files.stream()
                .map(fileId -> FileList.builder()
                        .fileId(fileId)
                        .fileRole(FileRoleEnum.RESOURCE.getFileRole())
                        .nodeId(resourceId)
                        .build())
                .collect(Collectors.toList());
        this.saveBatch(fileLists);
    }
    public void deleteAllResourceFileList(Long resourceId) {
        this.remove(new LambdaQueryWrapper<FileList>().eq(FileList::getNodeId, resourceId));
    }

    public List<Long> getResourceFileIds(Long resourceId) {
        return this.lambdaQuery()
                .eq(FileList::getNodeId, resourceId)
                .eq(FileList::getFileRole, FileRoleEnum.RESOURCE.getFileRole())
                .list()
                .stream()
                .map(FileList::getFileId)
                .collect(Collectors.toList());
    }
}
