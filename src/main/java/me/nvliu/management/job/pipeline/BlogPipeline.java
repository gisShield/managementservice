package me.nvliu.management.job.pipeline;

import me.nvliu.management.entity.Blog;
import me.nvliu.management.job.processor.BlogProcessor;
import me.nvliu.management.repository.BlogRepository;
import me.nvliu.management.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.job.pipeline
 * @Description:
 * @Date: Create in 9:30 2018/9/17
 * @Modified By:
 */
@Repository
public class BlogPipeline implements Pipeline {
    private final static Logger log = LoggerFactory.getLogger(BlogPipeline.class);

    @Autowired
    private BlogRepository blogRepository;
    @Override
    public void process(ResultItems resultItems, Task task) {
        Blog newBlog  ;
        Optional<Blog> re;
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().contains("W_")) {
                newBlog=(Blog) entry.getValue();
                log.info("data = [" + newBlog + "]");
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
