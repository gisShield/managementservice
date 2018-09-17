package me.nvliu.management.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:mvp
 * @ProjectName: restservice
 * @PacketName: com.nvliu.restservice.util
 * @Description:
 * @Date: Create in 16:31 2018/1/28
 * @Modified By:
 */
public class HttpUtils {
    protected static Logger log = LoggerFactory.getLogger(HttpUtils.class);
    /**
     * 发送get请求
     * @param url    路径
     * @return
     */
    public static String httpGet(String url){
        //get请求返回结果
        JSONObject jsonResult = null;
        String allUrl = url;
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            // 连接超时时间（默认10秒 10000ms） 单位毫秒（ms）
            int connectionTimeout = 3000;
            // 读取数据超时时间（默认30秒 30000ms） 单位毫秒（ms）
            int soTimeout = 10000;
            //发送get请求
            String allUrl1="https://tu.nvliu.me/portalapi/1/upload/?key=9710c27016b6a2bc2bef2bbf96845fd1&source="+allUrl+"&format=json";
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectionTimeout)
                    .setSocketTimeout(soTimeout).build();

            HttpGet request = new HttpGet(allUrl1);
            request.setConfig(config);
            HttpResponse response = closeableHttpClient.execute(request);
            log.info("原url = [" + url + "]");
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == 200) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.parseObject(strResult);
                allUrl = jsonResult.getJSONObject("image").getString("url");
                //System.out.println("url = [" + allUrl + "]");
                log.info("转换后url = [" + allUrl + "]");
            } else {
                log.info("响应值："+response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return allUrl;
    }

    public static String httpPost(String url){
        //get请求返回结果
        JSONObject jsonResult = null;
        String allUrl = url;
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            // 连接超时时间（默认10秒 10000ms） 单位毫秒（ms）
            int connectionTimeout = 3000;
            // 读取数据超时时间（默认30秒 30000ms） 单位毫秒（ms）
            int soTimeout = 10000;
            //发送get请求
//            String allUrl1="https://tu.nvliu.me/portalapi/1/upload/?key=9710c27016b6a2bc2bef2bbf96845fd1&source="+allUrl+"&format=json";
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(connectionTimeout)
                    .setSocketTimeout(soTimeout).build();

            HttpPost request = new HttpPost("https://tu.nvliu.me/portalapi/1/upload");

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("key", "9710c27016b6a2bc2bef2bbf96845fd1"));
            nvps.add(new BasicNameValuePair("source", allUrl));
            nvps.add(new BasicNameValuePair("format", "json"));

            //设置参数到请求对象中
            request.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));


            request.setConfig(config);
            HttpResponse response = closeableHttpClient.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == 200) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.parseObject(strResult);
                allUrl = jsonResult.getJSONObject("image").getString("url");
                //System.out.println("url = [" + allUrl + "]");
                log.info("url = [" + allUrl + "]");
            } else {
                log.info("响应值："+response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return allUrl;
    }

    public static void main (String[] args) {
        httpGet("http://imgs.aixifan.com/content/2018_05_20/1526815774.jpg" );

    }
}
