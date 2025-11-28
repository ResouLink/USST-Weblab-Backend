package com.weblab.server.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.weblab.server.entity.File;
import com.weblab.server.mapper.FileMapper;
import org.springframework.stereotype.Service;

@Service
public class FileDao extends ServiceImpl<FileMapper, File> {
}
