package me.nvliu.management.auditing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.constants.ConfigConstants;
import me.nvliu.management.utils.BlogCookieUtil;
import me.nvliu.management.utils.HexUtil;
import me.nvliu.management.utils.LoginHttpClient;
import me.nvliu.management.utils.Tools;
import org.apache.http.client.CookieStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.auditing
 * @Description: 微博登录接口
 * @Date: Create in 10:34 2018/9/28
 * @Modified By:
 */
public class BlogLogin {
    /*private String userName = ConfigConstants.B_USERNAME;
    private String pwd = ConfigConstants.B_PASSWORD;*/
    private String userName;
    private String pwd;
    private CookieStore cookies;
    private JSONObject preData;
    private String uid;

    public String getUid() {
        return uid;
    }

    /**
     * 提取小括号内的数据
     */
    public static Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
    /**
     * 提取第一次跳转的路径
     */
    public static Pattern patternPost = Pattern.compile("location\\.replace\\(\"(.*?)\"\\)");
    /**
     * 提取跳转成功后的四个链接
     */
    public static Pattern patternUrl = Pattern.compile("\"arrURL\":\\[\"(.*?)\",\"(.*?)\",\"(.*?)\",\"(.*?)\"\\]");
    public static Pattern patternManageUrl = Pattern.compile("bpfilter=([\\s\\S]*?) node-type");
    public static Pattern patternSE = Pattern.compile("tanx_s.src = \"([\\s\\S]*?)\";");
    public static Pattern patternTS = Pattern.compile("_src=([\\s\\S]*?) style");
    private final static Logger log = LoggerFactory.getLogger(BlogLogin.class);

    public BlogLogin(String userName,String pwd) {
//        this.preData = getPreLoginData();
        this.userName = userName;
        this.pwd = pwd;
    }

    public BlogLogin() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public CookieStore getCookies() {
        return cookies;
    }

    public void setCookies(CookieStore cookies) {
        this.cookies = cookies;
    }

    public JSONObject getPreData() {
        return preData;
    }

    public void setPreData(JSONObject preData) {
        this.preData = preData;
    }

    /**
     * 获取登录前的准备数据
     * @return
     */
    public JSONObject getPreLoginData() {
        //get请求返回结果
        log.info(LoginHttpClient.get("https://weibo.com/"));
        LoginHttpClient.printCookies();
        BlogCookieUtil.getULV();
        BlogCookieUtil.getApache();
        JSONObject jsonResult = null;
        String allUrl = "https://login.sina.com.cn/sso/prelogin.php?entry=weibo&callback=sinaSSOController.preloginCallBack" +
                "&rsakt=mod&checkpin=1&client=ssologin.js(v1.4.19)&su=" + getSu()
                + "&_=" + (System.currentTimeMillis() * 1000);
        try {
            String strResult = LoginHttpClient.get(allUrl);
            Matcher m = pattern.matcher(strResult);
            if (m.find()) {
                String result = m.group();
                jsonResult = JSONObject.parseObject(result);
                System.out.println("预处理："+result);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return jsonResult;
    }

    /**
     * 对用户名进行加密
     * @return
     */
    public String getSu() {
        try {
            String urlname = URLEncoder.encode(userName, "utf-8");
            String name = Base64.getEncoder().encodeToString(urlname.getBytes());
            System.out.println("名称：" + name);
            return name;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对密码进行加密
     * @return
     */
    public String getSp() {
        String t = "10001";
        String message = this.preData.getString("servertime") + "\t" + this.preData.getString("nonce") + "\n" + this.pwd;
        String result = null;
        try {
            result = rsa(this.preData.getString("pubkey"), t, message);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("RSA加密后的密码：" + result);
        return result;
    }

    /**
     * 对密码和参数进行rsa加密
     * @param pubkey 从准备数据中得到的公钥
     * @param exponentHex 加密的指数
     * @param pwd 密码和参数的字符串
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     */
    public static String rsa(String pubkey, String exponentHex, String pwd)
            throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException,
            UnsupportedEncodingException {
        KeyFactory factory = KeyFactory.getInstance("RSA");

        BigInteger m = new BigInteger(pubkey, 16);
        BigInteger e = new BigInteger(exponentHex, 16);
        RSAPublicKeySpec spec = new RSAPublicKeySpec(m, e);

//创建公钥
        RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);
        Cipher enc = Cipher.getInstance("RSA");
        enc.init(Cipher.ENCRYPT_MODE, pub);

        byte[] encryptedContentKey = enc.doFinal(pwd.getBytes("UTF-8"));

        return new String(HexUtil.encodeHex(encryptedContentKey));
    }

    /**
     * 执行登录操作
     * @return
     */
    public String login() {
        String url = "https://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.4.19)";
        try {
            Map<String, Object> map = new HashMap();
            map.put("entry", "weibo");
            map.put("gateway", "1");
            map.put("from", "");
            map.put("savestate", "7");
            map.put("qrcode_flag", "false");
            map.put("useticket", "1");
            map.put("pagerefer", "");
            map.put("vsnf", "1");
            map.put("su", getSu());

            map.put("service", "miniblog");
            map.put("servertime", this.preData.getString("servertime"));
            map.put("nonce", this.preData.getString("nonce"));
            map.put("pwencode", "rsa2");
            map.put("rsakv", this.preData.getString("rsakv"));
            map.put("sp", getSp());
            map.put("sr", "1920*1080");
            map.put("encoding", "UTF-8");
            map.put("prelt", "379");
            if(this.preData.containsKey("showpin") && this.preData.getIntValue("showpin") == 1){
                // 设置验证码
                map.put("pcid",this.preData.getString("pcid"));
                map.put("door",this.preData.getString("door"));
            }
            map.put("url", "https://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
            map.put("returntype", "META");
            //设置参数到请求对象中
            String strResult = LoginHttpClient.post(url, map);
            System.out.println("strResult = [" + strResult + "]");
            Matcher m = patternPost.matcher(strResult);
            if (m.find()) {
                String result = m.group();
                System.out.println("result = [" + result + "]");
                if (result.contains("reason=")) {
                    // 报错
                    String errInfo = result.substring(result.indexOf("reason=") + 7);
                    errInfo = URLDecoder.decode(errInfo, "GBK");
                    System.out.println("请求出错：" + errInfo);
                } else {
                    System.out.println("post请求之后：" + result);
                    Matcher m_u1 = pattern.matcher(result);
                    if (m_u1.find()) {
                        String location_url = m_u1.group();
                        location_url = URLDecoder.decode(location_url, "utf-8");
                        location_url = location_url.substring(1, location_url.length() - 1);
                        System.out.println("跳转的url:" + location_url);
                        String urls = LoginHttpClient.get(location_url);
                        if (Tools.notEmpty(urls)) {
                            Matcher m2 = patternUrl.matcher(urls);
                            if (m2.find()) {
                                String result2 = m2.group();
                                result2 = result2.substring(10, result2.length() - 1);
                                log.info(result2);
                                for (String u : result2.split(",")) {
                                    u = u.replaceAll("\\\\", "");
                                    log.info("请求链接："+u);
                                    log.info(LoginHttpClient.get(u.substring(1, u.length() - 1)));
                                }
                                String info = LoginHttpClient.get("https://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack&sudaref=weibo.com");
                                log.info(info);
                                LoginHttpClient.addCookie("un",this.userName,".weibo.com","/");
                                LoginHttpClient.addCookie("wvr","6",".weibo.com","/");
                                LoginHttpClient.addCookie("cross_origin_proto","SSL",".weibo.com","/");
                                Matcher m_info = pattern.matcher(info);
                                if(m_info.find()){
                                    info = m_info.group();
                                    JSONObject userInfo = JSON.parseObject(info);
                                    if("true".equals(userInfo.get("result").toString().toLowerCase())){
                                        this.uid = userInfo.getJSONObject("userinfo").getString("uniqueid");
                                        return "T";
                                    }
                                }
                                log.info("登录失败：");
                                log.info(info);
                                return "F";
                                /*Matcher m_info = pattern.matcher(info);
                                if(m_info.find()){
                                    info = m_info.group();
                                    JSONObject userInfo = JSON.parseObject(info);
                                    log.info("ajaxlogin.php:"+info);
                                    if("true".equals(userInfo.get("result").toString().toLowerCase())){
                                        log.info(LoginHttpClient.get("https://weibo.com/u/"+userInfo.getJSONObject("userinfo").getString("uniqueid")+"/home"+userInfo.getJSONObject("userinfo").getString("userdomain")));
                                        String src = LoginHttpClient.get("https://j.s.weibo.com/widget.html?t=1539831805448117826");
                                        Matcher m_src = patternSE.matcher(src);
                                        if(m_src.find()){
                                            src = m_src.group();
                                            src = src.substring(14,src.length()-2);
                                            log.info(LoginHttpClient.get(src));
                                            String str_topic = LoginHttpClient.get("https://weibo.com/p/10080850b1c3b64e5545118a102f555513c8e2?k=女流&_from_=huati_topic");
                                            log.info(str_topic);
                                            String topic_home = LoginHttpClient.get("https://weibo.com/p/10080850b1c3b64e5545118a102f555513c8e2/super_index");
                                            log.info(topic_home);
                                            Matcher m_topic_manage = patternManageUrl.matcher(topic_home);
                                            while (m_topic_manage.find()){
                                                String str = m_topic_manage.group();
                                                if (str.contains("manage")){
                                                    String href = str.split("\\s+")[1];
                                                    log.info(href);
                                                    href = href.substring(7,href.length()-3).replaceAll("\\\\", "");
                                                    String str_manage = LoginHttpClient.get("https://weibo.com"+href);
                                                    log.info(str_manage);
                                                    Matcher m_totalstatus = patternTS.matcher(str_manage);
                                                    if (m_totalstatus.find()){
                                                        String totalstatus_url = m_totalstatus.group();
                                                        totalstatus_url = totalstatus_url.substring(7,totalstatus_url.length()-7).replaceAll("\\\\", "");
                                                        log.info(LoginHttpClient.get("https:"+totalstatus_url));

                                                    }
                                                }
                                            }
//                                            LoginHttpClient.get("https://weibo.com/p/10080850b1c3b64e5545118a102f555513c8e2/manage?from=page_100808&mod=TAB&pids=plc_main&ajaxpagelet=1&ajaxpagelet_v6=1&__ref=%2Fp%2F10080850b1c3b64e5545118a102f555513c8e2%2Fsuper_index&_t=FM_153983184793917")
                                        }
                                    }
                                }*/

                                /*LoginHttpClient.addCookie("Apache","3924843307611.9004.1539739122472");
                                LoginHttpClient.addCookie("SINAGLOBAL","6290863793975.597.1517572462779");
                                LoginHttpClient.addCookie("_s_tentry","login.sina.com.cn");
                                LoginHttpClient.addCookie("cross_origin_proto","SSL");
                                LoginHttpClient.addCookie("login_sid_t","103a9895806d689c8d10b5dec419bd86");
                                */
                                /*LoginHttpClient.addCookie("un",this.userName);
                                LoginHttpClient.addCookie("wvr","6");
                                LoginHttpClient.addCookie("cross_origin_proto","SSL");
                                LoginHttpClient.addCookie("Apache","3463151086939.591.1539590401640");
                                LoginHttpClient.addCookie("login_sid_t","82ea779f59a307bede5c748b9421c641");


                                log.info(LoginHttpClient.get("https://weibo.com/p/10080850b1c3b64e5545118a102f555513c8e2/manage"));
                                log.info(LoginHttpClient.get("https://weibo.com/p/10080850b1c3b64e5545118a102f555513c8e2/manage?from=page_100808&mod=TAB&pids=plc_main&ajaxpagelet=1&ajaxpagelet_v6=1&__ref=/p/10080850b1c3b64e5545118a102f555513c8e2/super_index"));
                                log.info(LoginHttpClient.get("https://huati.weibo.com/manage/totalstatus?page_id=10080850b1c3b64e5545118a102f555513c8e2&time=1539756000&sign=80b2938b631b61988b842fcc313c94fb&fs=&flid=&_random=153975696260228"));
                                *//*LoginHttpClient.printCookies();*/
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return "F";
        }
        return "F";
    }

    public String keepCookie(String url){
        String str = LoginHttpClient.get(url);
        return str;
    }
    public String blockBlog(String id){
        Map map = new HashMap();
        map.put("mid",id);
        map.put("object_id", ConfigConstants.TOPIC_ID);
        LoginHttpClient.printCookies();
        String res = LoginHttpClient.post("https://huati.weibo.com/aj_manage/shieldstatussingle",map);
        return res;
    }

    public static void main(String[] args) {
        /*BlogLogin blogLogin = new BlogLogin();
        blogLogin.login("");*/
//        System.out.println("args = [" + blogLogin.ocr("") + "]");
    }
}
