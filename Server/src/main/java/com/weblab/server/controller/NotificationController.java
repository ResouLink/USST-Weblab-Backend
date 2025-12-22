package com.weblab.server.controller;

import com.weblab.common.result.ApiResult;
import com.weblab.server.entity.Notification;
import com.weblab.server.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 获取当前用户的未读通知数量
     */
    @GetMapping("/unread/count")
    public ApiResult getUnreadCount() {
        try {
            Long userId = getCurrentUserId();
            long count = notificationService.getUnreadCount(userId);
            log.info("用户{}未读通知数: {}", userId, count);
            return ApiResult.success(count);
        } catch (Exception e) {
            log.error("获取未读通知数失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    /**
     * 获取当前用户的通知列表
     *
     * @param status 状态过滤：null=全部，0=未读，1=已读
     */
    @GetMapping
    public ApiResult getNotifications(@RequestParam(required = false) Integer status) {
        try {
            Long userId = getCurrentUserId();
            List<Notification> notifications = notificationService.getNotificationsByUserId(userId, status);
            log.info("用户{}获取通知列表，状态: {}, 条数: {}", userId, status, notifications.size());
            return ApiResult.success(notifications);
        } catch (Exception e) {
            log.error("获取通知列表失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    /**
     * 标记指定通知为已读
     */
    @PutMapping("/{id}/read")
    public ApiResult markAsRead(@PathVariable Long id) {
        try {
            Long userId = getCurrentUserId();
            notificationService.markAsRead(id);
            log.info("用户{}标记通知{}为已读", userId, id);
            return ApiResult.success("通知已标记为已读");
        } catch (Exception e) {
            log.error("标记通知为已读失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    /**
     * 标记当前用户的所有通知为已读
     */
    @PutMapping("/read/all")
    public ApiResult markAllAsRead() {
        try {
            Long userId = getCurrentUserId();
            notificationService.markAllAsRead(userId);
            log.info("用户{}已标记所有通知为已读", userId);
            return ApiResult.success("所有通知已标记为已读");
        } catch (Exception e) {
            log.error("标记所有通知为已读失败", e);
            return ApiResult.fail(e.getMessage());
        }
    }

    /**
     * 获取当前登录用户的ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("用户未认证");
        }
        // 从 authentication.getPrincipal() 获取 UserDetails
        Object principal = authentication.getPrincipal();
        if (principal instanceof com.weblab.server.security.LoginUser) {
            return ((com.weblab.server.security.LoginUser) principal).getUser().getId();
        }
        throw new RuntimeException("无法获取用户信息");
    }
}

