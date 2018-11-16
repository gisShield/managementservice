package me.nvliu.management.utils;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.utils
 * @Description: 登录 http 类 会保存会话cookie
 * @Date: Create in 22:25 2018/9/28
 * @Modified By:
 */
public class LoginHttpClient {
    private final static Logger LOG = LoggerFactory.getLogger(LoginHttpClient.class);
    /** 请求网站的编码，这个地方，我默认 写的是GB3212*/
    private static final String DEFALUT_ENCODE = "UTF-8";

    public static CloseableHttpClient httpClient = null;
    public static HttpClientContext context = null;
    public static CookieStore cookieStore = null;
    public static RequestConfig requestConfig = null;
    public static String agent = null;

    static {
        init();
    }

    private static void init() {
        context = HttpClientContext.create();
        cookieStore = new BasicCookieStore();
        // 配置超时时间（连接服务端超时1秒，请求数据返回超时2秒）
        requestConfig = RequestConfig.custom().setConnectTimeout(120000).setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000).build();
        // 设置默认跳转以及存储cookie
        httpClient = HttpClientBuilder.create().setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore).build();
        agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36";
    }


    /**
     * 发送get请求
     *
     * @param url
     * @return response
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String get(String url)  {
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("User-Agent", agent);

        CloseableHttpResponse response = null;
        try {
            //设定请求的参数
            response= httpClient.execute(httpget, context);

            return copyResponse2Str(response);
        } catch(Exception e){
            LOG.debug("请求失败\t"+url);
        }finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的Response转化成String对象
     * @param response 返回的Response
     * @return
     */
    private static String copyResponse2Str(CloseableHttpResponse response){
        try {
            int code = response.getStatusLine().getStatusCode();
            //当请求的code返回值不是400的情况
            if((code == HttpStatus.SC_MOVED_TEMPORARILY )
                    || (code == HttpStatus.SC_MOVED_PERMANENTLY)
                    || (code == HttpStatus.SC_SEE_OTHER)
                    || (code == HttpStatus.SC_TEMPORARY_REDIRECT)) {
                return null;
            }else{
                return copyInputStream2Str(response.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 将InputStream转化为String类型的数据
     * @param in
     * @return
     */
    private static String copyInputStream2Str(InputStream in){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,DEFALUT_ENCODE));
            String line = null;
            StringBuffer sb = new StringBuffer();
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            LOG.debug("获取字符串失败");
        }
        return null;
    }

    /**
     * 发送post请求，不带参数 的post
     * @param url
     * @return
     */
    public static String post(String url){
        return post(url, null);
    }

    /**
     * 发从post 请求
     * @param url
     * @param parameters
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String post(String url, Map<String,Object> parameters){
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("User-Agent", agent);
        httpPost.setHeader("X-Requested-With","XMLHttpRequest");
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        CloseableHttpResponse response = null;
        try {
            //设定请求的参数
            setRequestParamter(parameters, httpPost);
            //发送请求
            response = httpClient.execute(httpPost, context);
            return copyResponse2Str(response);
        }catch(Exception e){
            LOG.debug("请求失败\t"+url);
        }finally {
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设定POST请求的参数
     * @param parameters
     * @param httpPost
     * @throws UnsupportedEncodingException
     */
    private static void setRequestParamter(Map<String, Object> parameters, HttpPost httpPost)
            throws UnsupportedEncodingException {
        List<NameValuePair> nvps;
        //添加参数
        if(parameters != null && parameters.size()>0){
            nvps = new ArrayList<NameValuePair>();
            for(Map.Entry<String, Object> map:parameters.entrySet()){
                NameValuePair param = new BasicNameValuePair(map.getKey(), map.getValue().toString());
                nvps.add(param);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, DEFALUT_ENCODE));
        }
    }

    /**
     * 将 http://www.yellowcong.com?age=7&name=8
     * 这种age=7&name=8  转化为map数据
     * @param parameters
     * @return
     */
    @SuppressWarnings("unused")
    private static List<NameValuePair> toNameValuePairList(String parameters) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        String[] paramList = parameters.split("&");
        for (String parm : paramList) {
            int index = -1;
            for (int i = 0; i < parm.length(); i++) {
                index = parm.indexOf("=");
                break;
            }
            String key = parm.substring(0, index);
            String value = parm.substring(++index, parm.length());
            nvps.add(new BasicNameValuePair(key, value));
        }
        System.out.println(nvps.toString());
        return nvps;
    }

    /**
     * 手动增加cookie
     * @param name
     * @param value
     * @param domain
     * @param path
     */
    public static void addCookie(String name, String value, String domain, String path) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookieStore.addCookie(cookie);
    }
    public static void addCookie(String name, String value, String domain, String path, Date date) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setExpiryDate(date);
        cookieStore.addCookie(cookie);
    }
    public static void addCookie(String name, String value) {
        BasicClientCookie cookie = new BasicClientCookie(name, value);
        cookieStore.addCookie(cookie);
    }

    /**
     * 把结果console出来
     *
     * @param httpResponse
     * @throws ParseException
     * @throws IOException
     */
    public static void printResponse(HttpResponse httpResponse) throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
    }

    /**
     * 把当前cookie从控制台输出出来
     *
     */
    public static void printCookies() {
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("key:" + cookie.getName() + "  value:" + cookie.getValue()+ "  domain:" + cookie.getDomain());
        }
    }

    /**
     * 检查cookie的键值是否包含传参
     *
     * @param key
     * @return
     */
    public static boolean checkCookie(String key) {
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        boolean res = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                res = true;
                break;
            }
        }
        return res;
    }
    public static String getCookieValue(String key) {
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        String res = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                res = cookie.getValue();
                break;
            }
        }
        return res;
    }
    public static String appendCookie(String key,String value) {
        cookieStore = context.getCookieStore();
        List<Cookie> cookies = cookieStore.getCookies();
        String oldValue = "";
        String path = "";
        String domain = "";
        Date expiry = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                oldValue = cookie.getValue();
                path = cookie.getPath();
                domain = cookie.getDomain();
                expiry = cookie.getExpiryDate();
                cookies.remove(cookie);
            }
        }
        addCookie(key,oldValue+value,domain,path,expiry);
        return oldValue+value;

    }

    /**
     * 直接把Response内的Entity内容转换成String
     *
     * @param httpResponse
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String toString(CloseableHttpResponse httpResponse) throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null){
            return EntityUtils.toString(entity);
        }else{
            return null;
        }
    }
}
