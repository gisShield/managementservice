package me.nvliu.management.job.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.entity.Blog;
import me.nvliu.management.job.pipeline.BlogPipeline;
import me.nvliu.management.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.job.processor
 * @Description:
 * @Date: Create in 17:32 2018/9/15
 * @Modified By:
 */
public class BlogProcessor implements PageProcessor {
    private final static Logger log = LoggerFactory.getLogger(BlogProcessor.class);
    private final  String[] USERAGENT ={
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299", // Edge 浏览器
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36", // chrome浏览器
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0", // 火狐
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36", // mac 上chrome浏览器
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.1 Safari/603.1.30" // Safari
    };
    private Site site = Site.me()
            .setDomain("https://m.weibo.cn")
            .setSleepTime(1000)
            .setRetryTimes(30)
            .setCharset("utf-8")
            .setTimeOut(30000);

    private String url;


    public BlogProcessor(String url){
        this.url = url;
        this.site.setUserAgent(USERAGENT[Tools.getRandomNum(0,4)]);
    }
    @Override
    public void process(Page page) {
        log.info("微博" +this.url +"页面抓取 ");
        if (page.getUrl().regex(this.url).match()) {
            if (page.getJson()!= null){
                JSONObject jsonObject = JSONObject.parseObject(page.getJson().toString());
                if (jsonObject !=null ){
                    if (jsonObject.get("ok").toString().equals("1")){
                        for (Object  o:jsonObject.getJSONObject("data").getJSONArray("cards")){
                            JSONObject jsonObj  = (JSONObject) o;
                            for(Object  o1:jsonObj.getJSONArray("card_group")){
                                JSONObject object  = (JSONObject) o1;
                                if(object.get("card_type").toString().equals("9")){
                                    Blog blog = setBlog(object);
                                    page.putField("W_"+blog.getBlogId(), blog);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public Date transformDate (String time){
        Calendar calendar = Calendar.getInstance();
        if(time.contains("刚刚")){
            return calendar.getTime();
        }else if(time.contains("秒")){
            int second = Integer.parseInt(time.substring(0,(time.length()-2)));
            calendar.add(Calendar.SECOND,-second);
            return calendar.getTime();
        }else if(time.contains("分")){
            int minute = Integer.parseInt(time.substring(0,(time.length()-3)));
            calendar.add(Calendar.MINUTE,-minute);
            return calendar.getTime();
        }else if(time.contains("时")){
            int hour = Integer.parseInt(time.substring(0,(time.length()-3)));
            calendar.add(Calendar.HOUR_OF_DAY,-hour);
            return calendar.getTime();
        }else if(time.contains("天")){
            // 1天以内
            if (time.contains("昨天")){
                try {
                    String [] arr = time.split("\\s+");
                    String hm = arr[arr.length-1];
                    calendar.add(Calendar.DAY_OF_MONTH,-1);
                    String realTime = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+hm+":00";
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return fmt.parse(realTime);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }

            }
        }else if(time.contains("-")){
            if(time.length() <= 5){ // 本年内
                try {
                    String realTime = calendar.get(Calendar.YEAR)+"-"+time+" 00:00:00";
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return fmt.parse(realTime);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }else{ // 本年之前
                try {
                    String realTime = time+" 00:00:00";
                    DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return fmt.parse(realTime);
                }catch (Exception e){
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }
    public static String filterEmoji(String source) {
        String output = null;
        if (source != null && source.length() > 0) {
            //剔出<html>的标签
            output = source.replaceAll("</?[^>]+>", "");
        }
        return output;
    }
    public Blog setBlog(JSONObject object){
        Blog newBlog = new Blog();
        JSONObject blog  = (JSONObject) object.get("mblog");
        newBlog.setBlogTime(transformDate(blog.getString("created_at")));
        newBlog.setBlogId(blog.getString("id"));
        newBlog.setBlogText(filterEmoji(blog.getString("text")));
        if(blog.containsKey("pics")){
            List<String> images = new ArrayList<>();
            JSONArray pics = blog.getJSONArray("pics");
            for (int i=0;i<pics.size();i++){
                images.add(pics.getJSONObject(i).getString("url"));
            }
            newBlog.setBlogImages(images);
        }
        JSONObject user  = (JSONObject) blog.get("user");
        newBlog.setBlogUserId(user.getString("id"));
        newBlog.setBlogUserName(user.getString("screen_name"));

        if(blog.containsKey("page_info")){
            JSONObject videos  = (JSONObject) blog.get("page_info");
            if("video".equals(videos.getString("type"))){
                newBlog.setBlogVideoImages(videos.getJSONObject("page_pic").getString("url"));
                newBlog.setBlogVideo(videos.getJSONObject("media_info").getString("stream_url"));
            }
        }

        if (blog.containsKey("retweeted_status")){
            // 转发
            JSONObject retweetedBlog  = (JSONObject) blog.get("retweeted_status");
            newBlog.setBlogSourceTime(transformDate(retweetedBlog.get("created_at").toString()));
            newBlog.setBlogSourceId(retweetedBlog.getString("id"));
            newBlog.setBlogSourceText(filterEmoji(retweetedBlog.getString("text")));

            JSONObject sUser  = (JSONObject) retweetedBlog.get("user");
            if(sUser!= null){
                newBlog.setBlogSourceUserId(sUser.getString("id"));
                newBlog.setBlogSourceUserName(sUser.getString("screen_name"));
            }
            if(retweetedBlog.containsKey("pics")){
                List<String> images = new ArrayList<>();
                JSONArray spics = retweetedBlog.getJSONArray("pics");
                for (int i=0;i<spics.size();i++){
                    images.add(spics.getJSONObject(i).getString("url"));
                }
                newBlog.setBlogSourceImages(images);
            }
            if(retweetedBlog.containsKey("page_info")){
                JSONObject retweetedVideos  = (JSONObject) retweetedBlog.get("page_info");
                if("video".equals(retweetedVideos.getString("type"))){
                    newBlog.setBlogVideoSourceImages(retweetedVideos.getJSONObject("page_pic").getString("url"));
                    newBlog.setBlogVideoSource(retweetedVideos.getJSONObject("media_info").getString("stream_url"));
                }
            }
        }
        return newBlog;
    }

}
