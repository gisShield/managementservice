package me.nvliu.management.job.thread;



import me.nvliu.management.entity.TaskConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * @Author:mvp
 * @ProjectName: restservice
 * @PacketName: com.nvliu.restservice.job.thread
 * @Description:
 * @Date: Create in 14:46 2018/6/26
 * @Modified By:
 */
public class TaskThreadPool {
    protected static Logger log = LoggerFactory.getLogger(TaskThreadPool.class);

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ScheduledFuture<?> scheduledFuture;

    public TaskThreadPool(TaskConf taskConf, Runnable runnable, ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
        this.scheduledFuture = this.threadPoolTaskScheduler.schedule(runnable, new TaskTrigger(taskConf.getCron()));
    }

    public void update(TaskConf taskConf, Runnable runnable){
        if(scheduledFuture != null){
            scheduledFuture.cancel(true);
        }
        this.scheduledFuture = threadPoolTaskScheduler.schedule(runnable, new TaskTrigger(taskConf.getCron()));
    }
    public void  stop(){
        if(scheduledFuture != null){
            scheduledFuture.cancel(true);
        }
    }

}
