package me.nvliu.management.job.processor;

import me.nvliu.management.utils.Tools;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            getList(page.getHtml().toString());
            /*Elements list = page.getHtml().getDocument().getElementById("thread_list").getElementsByTag("li");
            for (int i = 0  ;i<list.size();i++ ){
//                 log.info(list.get(i).html());
             }*/
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    private String getList(String str){
        Pattern pattern= Pattern.compile("/<!--[\\s\\S]*?--\\>/g");//正则表达式 匹配 aa或bb或bb
        Matcher matcher= pattern.matcher(str);
//        StringBuffer sb=new StringBuffer();
        while(matcher.find()){
//            matcher.appendReplacement(sb, replaceText);
            String s=matcher.group();//得到匹配的结果
            if(s.contains("id=\"thread_list\"")){
                log.info(s);
            }
        }
        return null;

    }
}
