package me.nvliu.management.job.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.entity.Blog;
import me.nvliu.management.entity.Video;
import me.nvliu.management.entity.YuBa;
import me.nvliu.management.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.job.processor
 * @Description:
 * @Date: Create in 14:47 2018/9/17
 * @Modified By:
 */
public class YuBaProcessor implements PageProcessor {

    private final static Logger log = LoggerFactory.getLogger(YuBaProcessor.class);
    private final  String[] USERAGENT ={
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299", // Edge 浏览器
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36", // chrome浏览器
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0", // 火狐
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36", // mac 上chrome浏览器
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.1 Safari/603.1.30" // Safari
    };
    private Site site = Site.me()
            .setDomain("https://yuba.douyu.com")
            .setSleepTime(1000)
            .setRetryTimes(30)
            .setCharset("utf-8")
            .setTimeOut(30000);
    private String url;

    public YuBaProcessor(String url){
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
                    if (jsonObject.get("status_code").toString().equals("200")){
                        for (Object  o:jsonObject.getJSONArray("data")){
                            JSONObject jsonObj  = (JSONObject) o;
                            if(jsonObj.getIntValue("father_gid")!= 515){
                                // 白名单过滤
                                YuBa yb = setYuba(jsonObj);
                                page.putField("Y_"+yb.getPostId(), yb);
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

    public YuBa setYuba(JSONObject jsonObject) {
        YuBa yuba = new YuBa();
        yuba.setGroupId(jsonObject.getString("group_id"));
        yuba.setPostId(jsonObject.getString("post_id"));
        yuba.setUserId(jsonObject.getString("uid"));
        yuba.setUserName(jsonObject.getString("nickname"));
        yuba.setTitle(jsonObject.getString("title"));
        yuba.setDescribe(jsonObject.getString("describe"));
        if(jsonObject.getJSONArray("imglist") !=null && jsonObject.getJSONArray("imglist").size()>0){
            List<String> list = new ArrayList<>();
            for(Object obejct:jsonObject.getJSONArray("imglist")){
                JSONObject jsonObject1 = (JSONObject)obejct;
                list.add(jsonObject1.getString("url"));
            }
            yuba.setImglist(list);
        }
        if(jsonObject.getJSONArray("video")!= null && jsonObject.getJSONArray("video").size()>0){
            List<Video> list = new ArrayList<>();
            for(Object obejct:jsonObject.getJSONArray("imglist")){
                JSONObject jsonObject1 = (JSONObject)obejct;
                Video video = new Video();
                video.setImage(jsonObject1.getString("thumb"));
                video.setTitle(jsonObject1.getString("title"));
                video.setUrl(jsonObject1.getString("player"));
                list.add(video);
            }
            yuba.setVideoList(list);
        }
        yuba.setTime(new Date());
        return yuba;
    }
}
