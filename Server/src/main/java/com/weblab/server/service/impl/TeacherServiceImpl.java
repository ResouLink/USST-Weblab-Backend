package com.weblab.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.weblab.server.dao.TeacherDao;
import com.weblab.server.dto.TeacherDTO;
import com.weblab.server.entity.Teacher;
import com.weblab.server.service.TeacherService;
import com.weblab.server.vo.TeacherVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherDao teacherDao;

    @Override
    public TeacherVO getById(Long id) {
        Teacher teacher = teacherDao.getById(id);
        TeacherVO teacherVO = new TeacherVO();
        BeanUtil.copyProperties(teacher, teacherVO);
        return teacherVO;
    }

}
