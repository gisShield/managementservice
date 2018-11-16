package me.nvliu.management.auditing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.constants.ConfigConstants;
import me.nvliu.management.utils.BdHttpUtil;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.auditing
 * @Description: 百度内容审核API 接口
 * @Date: Create in 17:28 2018/9/18
 * @Modified By:
 */
@Component
public class BaiduApi {
    public String tokin;
    private String ak ;
    private String sk ;
    private String tokenUrl;
    private String imgUrl;
    private String textUrl;

    public BaiduApi() {
        this.ak = ConfigConstants.B_AK;
        this.sk = ConfigConstants.B_SK;
        this.imgUrl = ConfigConstants.B_IMAGEURL;
        this.tokenUrl = ConfigConstants.B_TOKENURL;
        this.textUrl = ConfigConstants.B_TEXTURL;
        this.tokin = this.getAuth();

    }
    public void changeToken(){
        this.tokin = this.getAuth();
    }

    public String getTokin() {
        return tokin;
    }

    /**
     * 获得授权
     *
     * @return
     */
    public String getAuth() {
        String authHost = this.tokenUrl ;
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + this.ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + this.sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String token = jsonObject.getString("access_token");
            return token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * 组合服务审核接口
     *
     * @param url
     * @return
     */
    public String getAuditing(String url) {
        // 图像组合APIurl
        String imgCensorUrl = this.imgUrl;
        try {
            //请求参数
            Map<String, Object> sceneConf = new HashMap<String, Object>();
            Map<String, Object> ocrConf = new HashMap<String, Object>();
            ocrConf.put("recognize_granularity", "big");
            ocrConf.put("language_type", "CHN_ENG");
            ocrConf.put("detect_direction", true);
            ocrConf.put("detect_language", true);
            sceneConf.put("ocr", ocrConf);

            Map<String, Object> input = new HashMap<String, Object>();
            List<Object> scenes = new ArrayList<Object>();

            // orc文字识别
            scenes.add("ocr");
            //政治敏感识别
            //scenes.add("politician");
            //色情识别
            scenes.add("antiporn");
            //暴恐识别
            //scenes.add("terror");
            //恶心图像识别
            //scenes.add("disgust");
            //广告检测
            //scenes.add("watermark");


            String imageUrl = url;
            input.put("imgUrl", imageUrl);
            input.put("scenes", scenes);
            input.put("sceneConf", sceneConf);

            String params = JSON.toJSONString(input);
            System.out.println(params);

            String accessToken = this.tokin;
            String result = BdHttpUtil.post(imgCensorUrl, accessToken, "application/json", params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * 文本审核
     *
     * @param content
     * @return
     */
    public String getTextAuditing(String content) {
        // 请求url
        String censorUrl = "https://aip.baidubce.com/rest/2.0/antispam/v2/spam";
        try {

            String param = "content=" + content;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = this.tokin;

            String result = BdHttpUtil.post(censorUrl, accessToken, param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static void main(String[] args) {
        BaiduApi baiduApi = new BaiduApi();
         //baiduApi.getAuditing("https://wx1.sinaimg.cn/orj360/00718Wh6gy1fvofdenzjqj30a909ydgk.jpg");
                baiduApi.getTextAuditing("交友恋爱，走心走肾...让身体和灵魂不再孤单哦");
    }

}
