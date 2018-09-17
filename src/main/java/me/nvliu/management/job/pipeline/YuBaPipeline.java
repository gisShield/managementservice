package me.nvliu.management.job.pipeline;

import me.nvliu.management.entity.Blog;
import me.nvliu.management.entity.YuBa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.job.pipeline
 * @Description:
 * @Date: Create in 14:47 2018/9/17
 * @Modified By:
 */
@Repository
public class YuBaPipeline implements Pipeline {
    private final static Logger log = LoggerFactory.getLogger(YuBaPipeline.class);

    @Override
    public void process(ResultItems resultItems, Task task) {
        YuBa yuba ;
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().contains("Y_")) {
                yuba=(YuBa) entry.getValue();
                log.info("data = [" + yuba + "]");
//                re = blogRepository.findById(newBlog.getId());
                //System.out.println("data = [" + re.get("data") + "]");
//                log.info("data = [" + re.get("data") + "]");
                /*if (!re.isPresent()){
                    blogRepository.save(newBlog);
                }*/
            }

        }
    }
}
