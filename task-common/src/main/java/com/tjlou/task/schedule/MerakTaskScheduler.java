package com.tjlou.task.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MerakTaskScheduler {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    /**
     * 开启任务
     * Cron Example patterns:
     * <li>"0 0 * * * *" = the top of every hour of every day.</li>
     * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
     * <li>"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.</li>
     * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
     * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
     * @param task
     * @param cron
     */
    public void schedule(Runnable task, String cron){
        if(cron == null || "".equals(cron)) {
            //每一分钟
            cron = "0 * * * * *";
        }
        threadPoolTaskScheduler.schedule(task, new CronTrigger(cron));
    }

    public void schedule(Runnable task, Date date) {
        threadPoolTaskScheduler.schedule(task, date);
    }


    /**
     * 重置任务
     */
    public void reset() {
        threadPoolTaskScheduler.shutdown();
        threadPoolTaskScheduler.initialize();
    }

    /**
     * 关闭之后启动一个任务
     * @param task
     * @param cron
     */
    public void resetSchedule(Runnable task, String cron){
        shutdown();
        threadPoolTaskScheduler.initialize();
        schedule(task, cron);
    }

    /**
     * 关闭所有任务
     */
    public void shutdown(){
        threadPoolTaskScheduler.shutdown();
    }
}
