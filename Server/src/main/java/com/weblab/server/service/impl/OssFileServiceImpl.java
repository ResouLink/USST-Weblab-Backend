package com.weblab.server.service.impl;

import com.weblab.common.utils.AliOssUtil;
import com.weblab.server.dao.FileDao;
import com.weblab.server.entity.File;
import com.weblab.server.service.OssFileService;
import com.weblab.server.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OssFileServiceImpl implements OssFileService {

    private final FileDao fileDao;
    private final AliOssUtil aliOssUtil;

    @Override
    public FileVO uploadFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String objectName = UUID.randomUUID() + "." + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);

            File fileInfo = File.builder()
                    .fileUrl(filePath)
                    .fileSize(file.getSize())
                    .fileType(extension)
                    .build();
            fileDao.save(fileInfo);
            
            FileVO fileVO = FileVO.builder()
                    .fileId(fileInfo.getId())
                    .fileType(fileInfo.getFileType())
                    .fileSize(fileInfo.getFileSize())
                    .fileName(originalFilename)
                    .build();

            log.info("文件上传成功, 文件ID: {}", fileInfo.getId());
            return fileVO;

        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(long id) {
        File searchedFile = fileDao.getById(id);
        if (searchedFile == null) {
            log.warn("文件不存在, ID: {}", id);
            throw new RuntimeException("文件不存在");
        }

        try {
            String[] parts = searchedFile.getFileUrl().split("/");
            String filename = parts[parts.length - 1];
            aliOssUtil.delete(filename);
            fileDao.removeById(id);
            log.info("文件删除成功, 文件ID: {}", id);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new RuntimeException("删除文件失败: " + e.getMessage());
        }
    }
}
