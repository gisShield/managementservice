package me.nvliu.management.auditing;

import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.utils.HexUtil;
import me.nvliu.management.utils.LoginHttpClient;
import me.nvliu.management.utils.Tools;
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
    private String userName = "xxxx";
    private String pwd = "xxxx";
    private JSONObject preData;
    /**
     * 提取小括号内的数据
     */
    public static Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
    public static Pattern patternPost = Pattern.compile("location\\.replace\\(\"(.*?)\"\\)");
    public static Pattern patternUrl = Pattern.compile("\"arrURL\":\\[\"(.*?)\",\"(.*?)\",\"(.*?)\",\"(.*?)\"\\]");
    private final static Logger log = LoggerFactory.getLogger(BlogLogin.class);

    public BlogLogin() {
        this.preData = getPreLoginData();
    }

    public JSONObject getPreLoginData() {
        //get请求返回结果
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
                System.out.println(result);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return jsonResult;
    }

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

    public void login() {
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
            map.put("url", "https://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack");
            map.put("returntype", "META");
            //设置参数到请求对象中
            String strResult = LoginHttpClient.post(url, map);
            Matcher m = patternPost.matcher(strResult);
            if (m.find()) {
                String result = m.group();
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
                                for (String u : result2.split(",")) {
                                    u = u.replaceAll("\\\\", "");
                                    LoginHttpClient.get(u.substring(1, u.length() - 1));
                                }
                                LoginHttpClient.printCookies();
                                log.info(LoginHttpClient.get("https://weibo.com"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public static void main(String[] args) {
        BlogLogin blogLogin = new BlogLogin();
        blogLogin.login();
    }
}
