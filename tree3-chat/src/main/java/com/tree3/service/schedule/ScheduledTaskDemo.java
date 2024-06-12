package com.tree3.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * ！！！配置类添加 @EnableScheduling //开启定时任务功能
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/6/11 18:38 </p>
 */
@Slf4j
@Service
public class ScheduledTaskDemo {

    /**
     * 30秒一次
     */

    @Scheduled(cron = "0/30 * * * * *")
    public void scheduleDemo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        log.debug("scheduleDemo-time:{}", formatter.format(LocalDateTime.now()));
    }
}
