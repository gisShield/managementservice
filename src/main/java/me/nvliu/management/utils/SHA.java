package me.nvliu.management.utils;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {
	 /**  
     * 定义加密方式  
     */    
    private final static String KEY_SHA = "SHA";    
    private final static String KEY_SHA1 = "SHA-1";  
      
    /**  
     * 全局数组  
     */    
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",    
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };    
    
    /**  
     * 构造函数  
     */    
    public SHA() {    
    
    }    
    
    /**  
     * SHA 加密  
     * @param data 需要加密的字节数组  
     * @return 加密之后的字节数组  
     * @throws Exception  
     */    
    public static byte[] encryptSHA(byte[] data) throws Exception {    
        // 创建具有指定算法名称的信息摘要    
//        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);    
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA1);    
        // 使用指定的字节数组对摘要进行最后更新    
        sha.update(data);    
        // 完成摘要计算并返回    
        return sha.digest();    
    }    
    
    /**  
     * SHA 加密  
     * @param data 需要加密的字符串  
     * @return 加密之后的字符串  
     * @throws Exception  
     */    
    public static String encryptSHA(String data) throws Exception {    
        // 验证传入的字符串    
        if (data == null || data.equals("")) {    
            return "";    
        }    
        // 创建具有指定算法名称的信息摘要    
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);    
        // 使用指定的字节数组对摘要进行最后更新    
        sha.update(data.getBytes());    
        // 完成摘要计算    
        byte[] bytes = sha.digest();    
        // 将得到的字节数组变成字符串返回    
        return byteArrayToHexString(bytes);    
    }   
    
    public static String encryptSHA(String str,String ecryptCode) throws NoSuchAlgorithmException{
        if (str == null || str.equals("")) {    
            return "";    
        }    
        MessageDigest sha = MessageDigest.getInstance(ecryptCode);    
        sha.update(str.getBytes());    
        byte[] bytes = sha.digest();    
        return byteArrayToHexString(bytes);  
    }
    
    /**  
     * 将一个字节转化成十六进制形式的字符串  
     * @param b 字节数组  
     * @return 字符串  
     */    
    private static String byteToHexString(byte b) {    
        int ret = b;    
        //System.out.println("ret = " + ret);    
        if (ret < 0) {    
            ret += 256;    
        }    
        int m = ret / 16;    
        int n = ret % 16;    
        return hexDigits[m] + hexDigits[n];    
    }    
    
    /**  
     * 转换字节数组为十六进制字符串  
     * @param bytes 字节数组  
     * @return 十六进制字符串  
     */    
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();    
        for (int i = 0; i < bytes.length; i++) {    
            sb.append(byteToHexString(bytes[i]));    
        }    
        return sb.toString();    
    }
    public static String bytesToHex(byte[] bytes) {      StringBuffer sb = new StringBuffer();      for(int i = 0; i < bytes.length; i++) {          String hex = Integer.toHexString(bytes[i] & 0xFF);          if(hex.length() < 2){              sb.append(0);          }          sb.append(hex);      }      return sb.toString();  }

    public static void main(String[] args) throws Exception {
		/*System.out.println(SHA.encryptSHA("admin", "SHA-1"));
		System.out.println(SHA.encryptSHA("admin", "SHA"));
		System.out.println(SHA.encryptSHA("admin"));*/

		String url = "https://login.sina.com.cn/crossdomain2.php?action=login&entry=weibo&r=https%3A%2F%2Fpassport.weibo.com%2Fwbsso%2Flogin%3Fssosavestate%3D1569676543%26url%3Dhttps%253A%252F%252Fweibo.com%252Fajaxlogin.php%253Fframelogin%253D1%2526callback%253Dparent.sinaSSOController.feedBackUrlCallBack%26display%3D0%26ticket%3DST-NTUyMDIwNTcwMw%3D%3D-1538140543-yf-F7A39D7941A11C68261CA68F34FF4562-1%26retcode%3D0&login_time=1538140543&sign=f6148c794d347449&sr=1920%2A1080";
        url = URLDecoder.decode(url,"utf-8");
		System.out.println(url);
        /*String [] urls= url.split("https");
        StringBuffer stringBuffer = new StringBuffer();
		for(String u:urls){
		    String str = URLDecoder.decode("https"+u,"utf-8");
            System.out.println("u = [" + str + "]");
            stringBuffer.append(str);
        }
        System.out.println(stringBuffer.toString());*/
        System.out.println(url.substring(0,url.lastIndexOf("https")-5));

        /*String str = "https%253A%252F%252Fweibo.com%252Fajaxlogin.php%253Fframelogin%253D1%2526callback%253Dparent.sinaSSOController.feedBackUrlCallBack%26display%3D0%26ticket%3DST-NTUyMDIwNTcwMw%3D%3D-1538140543-yf-F7A39D7941A11C68261CA68F34FF4562-1%26retcode%3D0&login_time=1538140543&sign=f6148c794d347449&sr=1920%2A1080";
        System.out.println("u = [" + URLDecoder.decode(str,"utf-8") + "]");*/

    }
}
