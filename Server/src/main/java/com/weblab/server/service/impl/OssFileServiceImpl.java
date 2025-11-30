package com.weblab.server.service.impl;

import com.weblab.common.result.ApiResult;
import com.weblab.common.utils.AliOssUtil;
import com.weblab.server.dao.FileDao;
import com.weblab.server.entity.File;
import com.weblab.server.service.OssFileService;
import com.weblab.server.vo.FileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OssFileServiceImpl implements OssFileService {

    private final FileDao fileDao;
    private final AliOssUtil aliOssUtil;

    @Override
    public ApiResult uploadFile(MultipartFile file) {
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

            return ApiResult.success(fileVO);

        } catch (IOException e) {
            log.info("文件上传失败：{}",e);
        }

        return ApiResult.fail("文件上传失败");
    }

    @Override
    public ApiResult deleteFile(long id) {
        File searchedFile = fileDao.getById(id);
        if (searchedFile != null) {
            String[] parts = searchedFile.getFileUrl().split("/");
            String filename = parts[parts.length - 1]; // file.txt
            aliOssUtil.delete(filename);
            fileDao.removeById(id);
            return ApiResult.success("删除文件成功");
        }
        return ApiResult.fail("删除文件失败");
    }
}
