package me.nvliu.management.job.pipeline;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.auditing.BaiduApi;
import me.nvliu.management.auditing.BlogLogin;
import me.nvliu.management.constants.ConfigConstants;
import me.nvliu.management.entity.Blog;
import me.nvliu.management.repository.BlogRepository;
import me.nvliu.management.utils.TagUtils;
import me.nvliu.management.utils.Tools;
import me.nvliu.management.utils.sensi.SensitiveFilter;
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

    private BlogLogin blogLogin;

    /**
     * 设置过滤的微博 搞事部，月野兔
     */
    private List<String> filterUser = ConfigConstants.WHITE_USER_LIST;

    public void setBaiduApi(BaiduApi baiduApi) {
        this.baiduApi = baiduApi;
    }

    public void setBlogLogin(BlogLogin blogLogin) {
        this.blogLogin = blogLogin;
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
                        // 已经审核过
                        if(blog.getBlogTags()!= null && blog.getBlogTags().size()>0){

                        }else{
                            // 未审核
                            blog.setBlogTags(TagUtils.setTags(blog, baiduApi));
                        }
                        // 对审核结果进行解析
                        blockBlog(blog);
                    }
                }else{
                    //该微博不存在，为新微博
                    // 判断是否在过滤名单内，在的话直接保存
                    if(filterUser.contains(newBlog.getBlogUserId())){
                        newBlog.setIllegal("F");
                        blogRepository.save(newBlog);
                    }else {
                        //不在过滤名单执行审核保存
                        newBlog.setBlogTags(TagUtils.setTags(newBlog, baiduApi));
                        blockBlog(newBlog);
                    }
                }
            }
        }

    }

    private void blockBlog(Blog blog){
        String res = pushMessage(blog);
        if ("error".equals(res)) {

        } else if (Tools.notEmpty(res)) {
            // 执行封禁操作
            JSONObject jsonObject = JSON.parseObject(blogLogin.blockBlog(blog.getBlogId()));
            if(jsonObject.getIntValue("code") == 100000){
                blog.setIllegal("T");
            }
        } else {
            blog.setIllegal("F");
        }
        blogRepository.save(blog);
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
                        // spam 1表示违禁
                        // 2 需要人工审核
                        if (jsonObject.containsKey("spam") && jsonObject.getIntValue("spam") ==  1) {
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
            // 执行文本过滤判断
            String filter = filter(blog);
            return filter;
        }
    }

    /**
     * 过滤自定义关键词
     * @param blog
     * @return
     */
    private String filter(Blog blog){
        SensitiveFilter filter = SensitiveFilter.DEFAULT;
        String filted = null;
        String sentence = filter.delSpaceAndLineTag(blog.getBlogText().toLowerCase());
        if(sentence.contains("+") || sentence.contains("+")){
            String temp = filter.hasSensi(sentence, '*');
            if(temp != null){
                return temp;
            }
        }
        sentence= filter.delAllSymbol(sentence);
        filted = filter.hasSensi(sentence, '*');
        return filted;
    }
}
