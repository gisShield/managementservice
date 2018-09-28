package me.nvliu.management.job.pipeline;

import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.auditing.BaiduApi;
import me.nvliu.management.constants.ConfigConstants;
import me.nvliu.management.entity.Blog;
import me.nvliu.management.repository.BlogRepository;
import me.nvliu.management.utils.TagUtils;
import me.nvliu.management.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

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

    private BaiduApi baiduApi;

    public void setBaiduApi(BaiduApi baiduApi) {
        this.baiduApi = baiduApi;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        Blog newBlog;
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getKey().contains("W_")) {
                newBlog = (Blog) entry.getValue();
//                log.info("data = [" + newBlog + "]");
                Blog blog = blogRepository.findByBlogId(newBlog.getBlogId());
                if(Tools.notEmpty(blog)){
                    // 已经存在的数据
                    if(Tools.isEmpty(blog.isIllegal())){
                        blog.setBlogTags(TagUtils.setTags(blog, baiduApi));
                        String res = pushMessage(blog);
                        if ("error".equals(res)) {

                        } else if (Tools.notEmpty(res)) {
                            blog.setIllegal("T");
                            // 执行封禁操作
//                        newBlog.setBlock("T");
                        } else {
                            blog.setIllegal("F");
                        }
                        blogRepository.save(blog);
                    }
                }else{
                    //该微博不存在
                    newBlog.setBlogTags(TagUtils.setTags(newBlog, baiduApi));
                    String res = pushMessage(newBlog);
                    if ("error".equals(res)) {

                    } else if (Tools.notEmpty(res)) {
                        newBlog.setIllegal("T");
                        // 执行封禁操作
//                        newBlog.setBlock("T");
                    } else {
                        newBlog.setIllegal("F");
                    }
                    blogRepository.save(newBlog);
                }
            }
        }

    }


    private String pushMessage(Blog blog) {
        List<String> res = blog.getBlogTags();
        StringBuffer stringBuffer = new StringBuffer();
        if (res != null) {
            for (String item : res) {
                if(Tools.notEmpty(item)){
                    JSONObject jsonObject = JSONObject.parseObject(item);
                    if (jsonObject.containsKey("error_code")) {
                        return "error";
                    } else {
                        if (jsonObject.containsKey("spam") && jsonObject.getIntValue("spam") > 0) {
                            stringBuffer.append(ConfigConstants.TYPECODE_SPAM).append(",");
                        } else {
                            if (jsonObject.containsKey("politician") && Tools.notEmpty(jsonObject.get("politician"))) {
                                stringBuffer.append(ConfigConstants.TYPECODE_POLITICIAN).append(",");
                            }
                            if (jsonObject.containsKey("antiporn") && jsonObject.getIntValue("antiporn") > 0) {
                                stringBuffer.append(ConfigConstants.TYPECODE_ANTIPORN).append(",");
                            }
                            if (jsonObject.containsKey("terror") && jsonObject.getIntValue("terror") > 0) {
                                stringBuffer.append(ConfigConstants.TYPECODE_TERROR).append(",");
                            }
                            if (jsonObject.containsKey("disgust") && jsonObject.getIntValue("disgust") > 0) {
                                stringBuffer.append(ConfigConstants.TYPECODE_DISGUST).append(",");
                            }
                            if (jsonObject.containsKey("watermark") && Tools.notEmpty(jsonObject.getString("watermark"))) {
                                stringBuffer.append(ConfigConstants.TYPECODE_WATERMARK).append(",");
                            }

                        }
                    }
                }
            }
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.append(blog.getBlogId());
            //TODO 执行封禁操作
            return stringBuffer.toString();
        } else {
            return null;
        }
    }
}
