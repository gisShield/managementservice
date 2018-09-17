package me.nvliu.management.job.thread;


import me.nvliu.management.entity.TaskConf;
import me.nvliu.management.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName: restservice
 * @PacketName: com.nvliu.restservice.job.thread
 * @Description:
 * @Date: Create in 15:07 2018/6/26
 * @Modified By:
 */
public class TaskThreadPoolFactory {
    protected static Logger log = LoggerFactory.getLogger(TaskThreadPoolFactory.class);

    private static Map<Integer, TaskThreadPool> map = new HashMap<>(0);

    public static TaskThreadPool createTaskThreadPool(TaskConf taskConf, Runnable runnable, ThreadPoolTaskScheduler threadPoolTaskScheduler){
        TaskThreadPool taskThreadPool = new TaskThreadPool(taskConf,runnable,threadPoolTaskScheduler);
        map.put(taskConf.getId(),taskThreadPool);
        return  taskThreadPool;
    }

    public static TaskThreadPool getTaskThreadPool(int id){
        return map.get(id);
    }

    public static Boolean update(TaskConf taskConf,Runnable runnable){
        TaskThreadPool taskThreadPool = map.get(taskConf.getId());
        if (Tools.notEmpty(taskThreadPool)){
            try {
                taskThreadPool.update(taskConf,runnable);
                return true;
            }catch (Exception e){
                log.error(e.getMessage());
                return false;
            }

        }
        return false;
    }
    public static  Boolean stop(int id){
        TaskThreadPool taskThreadPool = map.get(id);
        if (Tools.notEmpty(taskThreadPool)){
            try {
                taskThreadPool.stop();
                map.remove(id);
                return true;
            }catch (Exception e){
                log.error(e.getMessage());
                return false;
            }
        }
        return false;
    }
}
