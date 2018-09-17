package me.nvliu.management.job.thread;

import me.nvliu.management.utils.Tools;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;

/**
 * @Author:mvp
 * @ProjectName: restservice
 * @PacketName: com.nvliu.restservice.job.thread
 * @Description:
 * @Date: Create in 15:24 2018/6/26
 * @Modified By:
 */
public class TaskTrigger implements Trigger {
    private  String cron;

    public TaskTrigger(String cron) {
        this.cron = cron;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        if(Tools.isEmpty(this.cron)){
            return null;
        }
        // 定时任务触发，可修改定时任务的执行周期
        CronTrigger trigger = new CronTrigger(cron);
        Date nextExecDate = trigger.nextExecutionTime(triggerContext);
        return nextExecDate;

    }
}
