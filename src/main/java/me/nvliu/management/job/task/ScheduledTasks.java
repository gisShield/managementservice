package me.nvliu.management.job.task;


import me.nvliu.management.job.pipeline.BlogPipeline;
import me.nvliu.management.job.pipeline.TieBaPipeline;
import me.nvliu.management.job.pipeline.YuBaPipeline;
import me.nvliu.management.job.processor.BlogProcessor;
import me.nvliu.management.job.processor.TieBaProcessor;
import me.nvliu.management.job.processor.YuBaProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;


/**
 * @Author:mvp
 * @ProjectName: restservice
 * @PacketName: com.nvliu.restservice.job.task
 * @Description:
 * @Date: Create in 21:15 2017/12/25
 * @Modified By:
 */
@Component
public class ScheduledTasks {

    @Value("${blog.topic.listurl}")
    private  String BLOG_URL ;

    @Value("${yuba.listurl}")
    private  String YUBA_URL ;

    @Value("${tieba.listurl}")
    private  String TIEBA_URL ;

    @Autowired
    private BlogPipeline blogPipeline;

    @Autowired
    private YuBaPipeline yuBaPipeline;

    @Autowired
    private TieBaPipeline tieBaPipeline;

    protected static Logger log = LoggerFactory.getLogger(ScheduledTasks.class);


    /*@Scheduled(cron = "${time.cron.blog}")
    public void getBlogInfo(){
        log.info("微博  数据抓取开始");
        String url = BLOG_URL+"?containerid=10080850b1c3b64e5545118a102f555513c8e2_-_sort_time&luicode=10000011&featurecode=20000320";
        Spider spider = initSpider(new BlogProcessor(BLOG_URL),url,blogPipeline);
        spider.start();
    }

    @Scheduled(cron = "${time.cron.yuba}")
    public void getYuBaInfo(){
        log.info("鱼吧  数据抓取开始");
        String url = YUBA_URL+"?group_id=557";
        Spider spider = initSpider(new YuBaProcessor(YUBA_URL),url,yuBaPipeline);
        spider.start();
    }*/

    @Scheduled(cron = "${time.cron.tieba}")
    public void getTieBaInfo(){
        log.info("贴吧  数据抓取开始");
        String url = TIEBA_URL+"?kw=女流";
        Spider spider = initSpider(new TieBaProcessor(TIEBA_URL),url,tieBaPipeline);
        spider.start();
    }


    public Spider initSpider(PageProcessor pageProcessor, String url, Pipeline pipeline){
        Spider spider= Spider.create(pageProcessor);
        spider.addUrl(url);
        spider.addPipeline(pipeline);
        spider.thread(5);
        spider.setExitWhenComplete(true);
        return spider;
    }
}


