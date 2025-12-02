package com.weblab.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.weblab.common.enums.FileRoleEnum;
import com.weblab.server.entity.FileList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileListMapper extends BaseMapper<FileList> {
    /**
     * 获取文件id
     * @param fileRole 文件角色
     * @param nodeId 节点id
     * @return
     */
    List<Long> getFileIds(FileRoleEnum fileRole, Long nodeId);



    @Select("""
    SELECT f.file_url
    FROM file_list fl
    LEFT JOIN file f
    ON fl.file_id = f.id
    WHERE fl.file_role = 3
      AND fl.node_id = #{nodeId}
    LIMIT 1
""")
    String selectAvatarUrlByNodeId(@Param("nodeId") Long nodeId);
}
