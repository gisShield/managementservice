package me.nvliu.management.job.pipeline;

import me.nvliu.management.entity.TieBa;
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
 * @Date: Create in 16:26 2018/9/17
 * @Modified By:
 */
@Repository
public class TieBaPipeline implements Pipeline {

    private final static Logger log = LoggerFactory.getLogger(TieBaPipeline.class);
    @Override
    public void process(ResultItems resultItems, Task task) {
        TieBa tieba ;
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().contains("T_")) {
                tieba=(TieBa) entry.getValue();
                log.info("data = [" + tieba + "]");
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
