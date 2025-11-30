package com.weblab.server.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FillHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createAt", LocalDateTime.class, getCurrentTime());
        this.strictInsertFill(metaObject, "updateAt", LocalDateTime.class, getCurrentTime());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateAt", LocalDateTime.class, getCurrentTime());
    }

    private LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
