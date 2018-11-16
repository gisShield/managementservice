package me.nvliu.management.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.utils
 * @Description:
 * @Date: Create in 16:54 2018/10/18
 * @Modified By:
 */
public class BlogCookieUtil {
    private final static String C = "Apache";
    private final static String K= "ULV";
    public static String getULV(){
        String D = hasKey(C);
        String az = hasKey(K);
        String[] ay = az.split(":");
        String aA = "";
        Calendar   i;
        if (ay.length >= 6) {
            if (!D.equals(ay[4])) {
                i =  Calendar.getInstance();
                Calendar e = Calendar.getInstance();
                e.setTime(new Date(Integer.parseInt(ay[0])));
                ay[1] = String.valueOf(Integer.parseInt(ay[1]) + 1);
                if (i.get(Calendar.MONTH) != e.get(Calendar.MONTH)) {
                    ay[2] = "1";
                } else {
                    ay[2] = String.valueOf(Integer.parseInt(ay[2]) + 1);
                } if (((i.getTimeInMillis() - e.getTimeInMillis()) / 86400000) >= 7) {
                    ay[3] = "1";
                } else {
                    if (i.get(Calendar.DAY_OF_MONTH) < e.get(Calendar.DAY_OF_MONTH)) {
                        ay[3] = "1";
                    } else {
                        ay[3] = String.valueOf(Integer.parseInt(ay[3]) + 1);
                    }
                }
                aA = ay[0] + ":" + ay[1] + ":" + ay[2] + ":" + ay[3];
                ay[5] = ay[0];
                ay[0] = String.valueOf(i.getTimeInMillis());
                addCookie(K, ay[0] + ":" + ay[1] + ":" + ay[2] + ":" + ay[3] + ":" + D + ":" + ay[5], 360,null);
            } else {
                aA = ay[5] + ":" + ay[1] + ":" + ay[2] + ":" + ay[3];
            }
        } else {
            i = Calendar.getInstance();
            aA = ":1:1:1";
            addCookie(K, i.getTimeInMillis() + aA + ":" + D + ":", 360,null);
        }
        return aA;
    }
    public static String getApache(){
        String e = Math.random() * 10000000000000L + "." + System.currentTimeMillis();
        addCookie(C,e,0,null);
        return null;
    }

    public static void addCookie(String az, String e, int i, String ay){
        if (e != null) {
            if (("undefined" == ay) || (null == ay)) {
                ay = "weibo.com";
            }
            if (i==0) {
                LoginHttpClient.addCookie(az,e,ay,"/");
            } else {

                Long aA = System.currentTimeMillis();
                aA = aA + 86400000L * i;
                LoginHttpClient.addCookie(az,e,ay,"/",DateUtil.timeStamp2Date(aA.toString()));
            }
        }
    }
    public static String hasKey(String key){
        if(Tools.notEmpty(key)){
            return LoginHttpClient.getCookieValue(key);
        }else{
            return "";
        }
    }
    public static String allKeys(String key,String D){
        if(LoginHttpClient.checkCookie(key)){
            //存在
            return LoginHttpClient.appendCookie(key,D);
        }else{
            //不存在
            return "";
        }
    }

    public static void main(String[] args) {
        getApache();
        getULV();
        LoginHttpClient.printCookies();
    }

}
