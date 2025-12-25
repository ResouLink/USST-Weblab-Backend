package com.weblab.server.controller;

import com.weblab.common.enums.RoleEnum;
import com.weblab.common.result.ApiResult;
import com.weblab.server.dto.ResourceDTO;
import com.weblab.server.security.SecurityUtil;
import com.weblab.server.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/resources")
public class ResourcesController {
    private final ResourceService resourceService;

    @PostMapping
    public ApiResult addResource(@RequestBody ResourceDTO resourceDTO) {
        try {
            if(resourceDTO.getUploaderId() == -1) {
                resourceDTO.setUploaderId(SecurityUtil.getLoginUser().getUser().getRoleId());
            }
            resourceService.addResource(resourceDTO);
            log.info("资源添加成功");
            return ApiResult.success("添加资源成功");
        } catch (Exception e) {
            log.error("资源添加失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResult updateResource(@PathVariable long id, @RequestBody ResourceDTO resourceDTO) {
        try {
            if(resourceDTO.getUploaderId() == -1) {
                resourceDTO.setUploaderId(SecurityUtil.getLoginUser().getUser().getRoleId());
            }
            resourceService.updateResource(resourceDTO, id);
            log.info("资源更新成功");
            return ApiResult.success("资源更新成功", 1);
        } catch (Exception e) {
            log.error("资源更新失败", e);
            return ApiResult.fail(1, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResult deleteResource(@PathVariable long id) {
        try {
            resourceService.deleteResource(id);
            log.info("资源删除成功");
            return ApiResult.success("资源删除成功");
        } catch (Exception e) {
            log.error("资源删除失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ApiResult getResource(@PathVariable long id) {
        try {
            return ApiResult.success(resourceService.getResourceById(id));
        } catch (Exception e) {
            log.error("获取资源失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping()
    public ApiResult getResources(@RequestParam long page,@RequestParam long size,@RequestParam(required = false) String keyword) {
        try {
            return ApiResult.success(resourceService.getResources(page,size,keyword));
        } catch (Exception e) {
            log.error("获取资源失败",e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @GetMapping("/courses/{courseId}")
    public ApiResult getResourcesByCourseId(@PathVariable long courseId) {
        try {
            return ApiResult.success(resourceService.getResourcesByCourseId(courseId));
        } catch (Exception e) {
            log.error("查询课程资源失败",e);
            return ApiResult.fail(e.getMessage());
        }
    }

    @PostMapping("/download/{id}")
    public ApiResult increaseCnt(@PathVariable long id) {
        try {
            resourceService.increaseDownloadCnt(id);
            log.info("资源下载次数+1成功");
            return ApiResult.success("资源下载次数+1成功");
        } catch (Exception e) {
            log.error("资源下载次数+1失败",e);
            return ApiResult.fail(e.getMessage());
        }
    }


    @GetMapping("/teachers/{teacherId}")
    public ApiResult getResourcesByTeacherId(@PathVariable long teacherId) {
        try {
            Long currentRoleId = SecurityUtil.getLoginUser().getUser().getRoleId();
            String role = SecurityUtil.getRole();
            if (teacherId == -1) {
                teacherId = currentRoleId;
            }
            if (RoleEnum.ADMIN.value().equals(role)) {
                return ApiResult.success(resourceService.getResourcesByTeacherId(teacherId));
            }
            if (RoleEnum.TEACHER.value().equals(role)) {
                if (!currentRoleId.equals(teacherId)) {
                    return ApiResult.fail("只能查询自己的资源");
                }
                return ApiResult.success(resourceService.getResourcesByTeacherId(teacherId));
            }
            return ApiResult.fail("无权限访问");
        } catch (Exception e) {
            log.error("教师查询资源失败", e);
            return ApiResult.fail("查询失败");
        }
    }


    @GetMapping("/students/{studentId}")
    public ApiResult getResourcesByStudentId(@PathVariable long studentId) {
        try {
            Long currentRoleId = SecurityUtil.getLoginUser().getUser().getRoleId();
            String role = SecurityUtil.getRole();
            if(studentId == -1) {
                studentId = SecurityUtil.getLoginUser().getUser().getRoleId();
            }
            if(RoleEnum.ADMIN.value().equals(role)) {
                return ApiResult.success(resourceService.getResourcesByStudentId(studentId));
            }
            if(RoleEnum.STUDENT.value().equals(role)) {
                if(!currentRoleId.equals(studentId)) {
                    return ApiResult.fail("只能查询自己的资源");
                }
                return ApiResult.success(resourceService.getResourcesByStudentId(studentId));
            }

            return ApiResult.fail("无权限访问");
        } catch (Exception e) {
            log.error("教师查询自己的资源失败",e);
            return ApiResult.fail(e.getMessage());
        }
    }
}
