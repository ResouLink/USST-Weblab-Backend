package com.weblab.server;



import com.weblab.server.dao.StudentDao;
import com.weblab.server.dto.StudentDTO;
import com.weblab.server.entity.Student;
import com.weblab.common.result.ApiResult;
import com.weblab.server.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudentServiceImplTest {

    @Autowired
    private StudentServiceImpl studentService;

    @Autowired
    private StudentDao studentDao;

    private Long testStudentId;

    @BeforeEach
    public void setUp() {
        // 清理数据库中可能存在的测试数据
        studentDao.remove(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Student>()
                .eq("name", "测试学生"));

        // 插入一条测试学生数据
        Student student = new Student();
        student.setName("测试学生");
        student.setCollege("测试学院");
        student.setMajor("测试专业");
        student.setGender(0);
        student.setCreateAt(LocalDateTime.now());
        student.setUpdateAt(LocalDateTime.now());

        studentDao.save(student);
        testStudentId = student.getId(); // 获取插入后的 ID
    }

    @Test
    public void testUpdateStudent() {
        // 构建 DTO
        StudentDTO dto = new StudentDTO();
        dto.setName("测试学生修改");
        dto.setCollege("修改学院");
        dto.setMajor("修改专业");
        dto.setGender(1);

        // 调用 updateStudent
        ApiResult result = studentService.updateStudent(dto, testStudentId);

        System.out.println("返回结果: " + result);

        // 验证更新是否成功
        Student updated = studentDao.getById(testStudentId);
        assertNotNull(updated);
        assertEquals("测试学生修改", updated.getName());
        assertEquals("修改学院", updated.getCollege());
        assertEquals("修改专业", updated.getMajor());
        assertEquals(1, updated.getGender());
    }

    @Test
    public void testUpdateNonExistingStudent() {
        // 尝试更新一个不存在的 ID
        StudentDTO dto = new StudentDTO();
        dto.setName("不存在的学生");

        ApiResult result = studentService.updateStudent(dto, 999999L);
        System.out.println("返回结果: " + result);

        assertEquals(1, result.getCode());
        assertEquals("学生不存在", result.getMessage());
    }
}
