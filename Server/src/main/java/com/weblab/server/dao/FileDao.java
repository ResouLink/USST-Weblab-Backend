package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.File;
import com.weblab.server.mapper.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileDao extends ServiceImpl<FileMapper, File> {
    public List<String> getFileUrls(List<Long> fileIds) {
        if(fileIds.isEmpty()) {
            return List.of();
        }
        return this.lambdaQuery()
                .in(File::getId, fileIds)
                .list()
                .stream()
                .map(File::getFileUrl)  // 假设字段叫 url
                .toList();
    }
}
