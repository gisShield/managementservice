package me.nvliu.management.job.taskrunnable;


import me.nvliu.management.entity.TaskConf;
import me.nvliu.management.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;


/**
 * @Author:mvp
 * @ProjectName: restservice
 * @PacketName: com.nvliu.restservice.job.taskrunnable
 * @Description:
 * @Date: Create in 16:50 2018/6/26
 * @Modified By:
 */
@Component
public class TaskRunnable implements Runnable {

    protected static Logger log = LoggerFactory.getLogger(TaskRunnable.class);
    private TaskConf taskConf;
    private PageProcessor pageProcessor;
    private Pipeline pipeline;
    private String runnableName;
    private TaskRepository taskRepository;

    public void setRunnableName(String runnableName) {
        this.runnableName = runnableName;
    }

    public void setTaskConf(TaskConf taskConf) {
        this.taskConf = taskConf;
    }

    public void setPageProcessor(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run() {
        Spider spider= Spider.create(this.pageProcessor);
        spider.addUrl(this.taskConf.getUrl());
        spider.addPipeline(this.pipeline);
        spider.thread(5);
        spider.setExitWhenComplete(true);
        spider.start();
        // 修改TaskConfig状态
        taskConf.setLastTimeExe(new Date());
        this.taskRepository.save(taskConf);

        log.info(this.runnableName+" 抓取线程开始执行！");
    }
}
