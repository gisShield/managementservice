package me.nvliu.management.job.processor;

import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.entity.TieBa;
import me.nvliu.management.entity.Video;
import me.nvliu.management.utils.Tools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
 * @Date: Create in 16:26 2018/9/17
 * @Modified By:
 */
public class TieBaProcessor implements PageProcessor {
    private final static Logger log = LoggerFactory.getLogger(TieBaProcessor.class);
    private final  String[] USERAGENT ={
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299", // Edge 浏览器
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36", // chrome浏览器
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0", // 火狐
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36", // mac 上chrome浏览器
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.1 Safari/603.1.30" // Safari
    };
    private Site site = Site.me()
            .setDomain("https://tieba.baidu.com")
            .setSleepTime(1000)
            .setRetryTimes(30)
            .setCharset("utf-8")
            .setTimeOut(30000);

    private String url;

    public TieBaProcessor(String url) {
        this.url = url;
        this.site.setUserAgent(USERAGENT[Tools.getRandomNum(0,4)]);
    }

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(this.url).match()) {
//            log.info(page.getHtml().toString());
            List<TieBa> tieBaList = getList(page.getHtml().toString());
            /*Elements list = page.getHtml().getDocument().getElementById("thread_list").getElementsByTag("li");
            for (int i = 0  ;i<list.size();i++ ){
//                 log.info(list.get(i).html());
             }*/
            if(tieBaList!=null){
                for(TieBa tieBa:tieBaList){
                    page.putField("T_"+tieBa.getPostId(), tieBa);
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    private List<TieBa> getList(String str){
        List<TieBa> tieBaList = new ArrayList<>();
        //正则表达式 匹配 aa或bb或bb
        log.info("调用正则匹配方法");
        // 先替换掉所有脚本文件
        str = str.replaceAll("<script[^>]*>[\\d\\D]*?</script>","");
        // 替换所有注释标签
        str = str.replaceAll("<!--","").replaceAll("-->","");
//        log.info(str);
        Elements list = Jsoup.parse(str).getElementById("thread_list").children();
        log.info("开始获得数据");
        for(Element liElement:list){
            TieBa tieBa = new TieBa();
            // li标签数据
            if(("thread_top_list_folder").equals(liElement.className())|| liElement.className().contains("thread_top")){
                continue;
            }
            String data = liElement.attr("data-field").replaceAll("&quot;","\"");
            JSONObject jsonObject = JSONObject.parseObject(data);
            tieBa.setPostId(jsonObject.getString("id"));

            String username = liElement.getElementsByClass("j_user_card").get(0).text();
            tieBa.setUserName(username);

            String userid = liElement.getElementsByClass("tb_icon_author").get(0).attr("data-field").replaceAll("&quot;","\"");
            jsonObject = JSONObject.parseObject(userid);
            tieBa.setUserId(jsonObject.getString("user_id"));

            String title = liElement.getElementsByClass("threadlist_lz").get(0).getElementsByTag("a").get(0).text();
            tieBa.setTitle(title);

            String de = liElement.getElementsByClass("threadlist_abs threadlist_abs_onlyline").text();
            tieBa.setDescribe(de);

            String time = liElement.getElementsByAttributeValue("title","创建时间").text();
//            log.info("time:"+time);

            List<String> images = new ArrayList<>();
            if(liElement.getElementById("fm"+tieBa.getPostId())!=null){
                Elements imageVideolist = liElement.getElementById("fm"+tieBa.getPostId()).getElementsByTag("li");
                for(Element image:imageVideolist){
                    Elements videosElements = image.getElementsByClass("threadlist_video");
                    if(image.getElementsByTag("img").size()>0){
                        String imgurl = image.getElementsByTag("img").get(0).attr("bpic");
                        images.add(imgurl);
                    }
                }
                tieBa.setImglist(images);

                List<Video> videos = new ArrayList<>();
                for(Element image:imageVideolist){
                    Elements videosElements = image.getElementsByClass("threadlist_video");
                    if(videosElements.size()>0){
                        Video video = new Video();
                        if(videosElements.get(0).getElementsByTag("img").size()>0){
                            String videoImage = videosElements.get(0).getElementsByTag("img").get(0).attr("src");
                            video.setImage(videoImage);
                        }
                        if(videosElements.get(0).getElementsByTag("video").size()>0){
                            String videourl = videosElements.get(0).getElementsByTag("video").get(0).attr("src");
                            video.setUrl(videourl);
                            video.setTitle(title);
                        }
                        if(video!=null){
                            videos.add(video);
                        }
                    }

                }
                tieBa.setVideoList(videos);
            }
            tieBa.setTime(new Date());
            tieBaList.add(tieBa);
        }
        return tieBaList;

    }
}
