package com.weblab.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weblab.server.dao.FileListDao;
import com.weblab.server.dao.UserDao;
import com.weblab.server.dto.UserRegisterDTO;
import com.weblab.server.dto.UserUpdateDTO;
import com.weblab.server.entity.FileList;
import com.weblab.server.entity.Users;
import com.weblab.server.service.OssFileService;
import com.weblab.server.service.StudentService;
import com.weblab.server.service.TeacherService;
import com.weblab.server.service.UserService;
import com.weblab.server.vo.FileVO;
import com.weblab.server.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final FileListDao fileListDao;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final OssFileService ossFileService;


    @Override
    public void addUserStudent(UserRegisterDTO userRegisterDTO) throws IOException {
        //todo为用户添加初始头像
        Users user = new Users();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setUserRole(1L);
        userDao.save(user);
        //添加默认头像
        ClassPathResource resource = new ClassPathResource("default_avatar.png");

        // 模拟 MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                resource.getFilename(),
                "image/jpeg",
                resource.getInputStream()
        );
        FileVO fileVO = ossFileService.uploadFile(multipartFile);
        fileListDao.save(new FileList().builder()
                .fileId(fileVO.getFileId())  //默认头像在附件表中的主键
                .fileRole(3) //文件角色：是头像
                .nodeId(user.getId()) //新创建用户的主键
                .build());
        log.info("学生用户添加成功");
    }

    @Override
    public void addUserTeacher(UserRegisterDTO userRegisterDTO) throws IOException {
        //todo为用户添加初始头像
        Users user = new Users();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setUserRole(0L);
        userDao.save(user);
        //添加默认头像
        ClassPathResource resource = new ClassPathResource("default_avatar.png");

        // 模拟 MultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                resource.getFilename(),
                "image/jpeg",
                resource.getInputStream()
        );
        FileVO fileVO = ossFileService.uploadFile(multipartFile);
        fileListDao.save(new FileList().builder()
                .fileId(fileVO.getFileId())  //默认头像在附件表中的主键
                .fileRole(3) //文件角色：是头像
                .nodeId(user.getId()) //新创建用户的主键
                .build());
        log.info("老师用户添加成功");
    }

    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO, long id) {
        Users existingUser = userDao.getById(id);
        if (existingUser == null) {
            log.warn("用户不存在");
            return;
        }
        BeanUtils.copyProperties(userUpdateDTO, existingUser);
        boolean update = userDao.updateById(existingUser);
        if (update && userUpdateDTO.getAvatarId() != null) {
            fileListDao.update(null, new LambdaUpdateWrapper<FileList>()
                    .eq(FileList::getFileRole, 3)
                    .eq(FileList::getNodeId, id)
                    .set(FileList::getFileId, userUpdateDTO.getAvatarId()));
            log.info("用户更新成功");
        } else {
            log.warn("用户更新失败");
        }
    }

    @Override
    public void deleteUser(long id) {
        Users existingUser = userDao.getById(id);
        userDao.removeById(id);
        //删除filelist中相关的记录（头像的）
        FileList existingfileList = fileListDao.getOne(new LambdaQueryWrapper<FileList>()
                .eq(FileList::getFileRole, 3)
                .eq(FileList::getNodeId, id));

        fileListDao.remove(new LambdaQueryWrapper<FileList>()
                .eq(FileList::getNodeId, id)
                .eq(FileList::getFileRole, 3));

        //删除头像的实际数据
        ossFileService.deleteFile(existingfileList.getFileId());
        if (existingUser.getRoleId() != null) {
            //是学生
            if (existingUser.getUserRole() == 1) {
                studentService.deleteStudent(existingUser.getRoleId());
            }
            //是老师
            else {
                teacherService.deleteTeacher(existingUser.getRoleId());
            }
        }
        //todo学习资源回答之类的删除要做，也可以不做
    }

    @Override
    public UserVO getUserById(long id) {
        UserVO userVO = new UserVO();
        Users user = userDao.getById(id);
        BeanUtils.copyProperties(user, userVO);
        userVO.setAvatarUrl(fileListDao.getAvatarUrl(id));
        return userVO;
    }

    @Override
    public List<UserVO> getUsers(long page, long size, String keyword) {
        // 1. 构建分页参数
        Page<Users> pageParam = new Page<>(page, size);

        // 2. 构建查询条件
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("username", keyword); // 根据 username 或 name 字段模糊查询
        }

        // 3. 分页查询
        Page<Users> resultPage = userDao.page(pageParam, queryWrapper);

        // 4. 转换为 VO 并查询头像 URL
        List<UserVO> voList = resultPage.getRecords().stream().map(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);

            // 查询头像 URL
            String avatarUrl = fileListDao.getAvatarUrl(user.getId());
            vo.setAvatarUrl(avatarUrl);

            return vo;
        }).collect(Collectors.toList());

        return voList;
    }

}
