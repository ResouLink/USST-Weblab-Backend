package com.weblab.server.event;

import com.weblab.server.entity.Notification;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationQueue {
    // 设置队列大小
    private static final int QUEUE_SIZE = 10000;

    private static final LinkedBlockingQueue<NotificationDto> blockingQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);

    // 向队列中添加元素
    public static boolean putNotification(NotificationDto notification) {
        try {
            blockingQueue.put(notification); // 阻塞方法
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    // 从队列中获取元素
    public static NotificationDto takeNotification() throws InterruptedException {
        return blockingQueue.take();
    }
}
