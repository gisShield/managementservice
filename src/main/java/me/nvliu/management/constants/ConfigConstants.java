package me.nvliu.management.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.constants
 * @Description:
 * @Date: Create in 16:03 2018/9/26
 * @Modified By:
 */
public final class ConfigConstants {
    public static final String B_AK = "xx";
    public  static final String B_SK = "xx" ;
    public   static final String B_TOKENURL = "https://aip.baidubce.com/oauth/2.0/token?";
    public  static final String B_IMAGEURL = "https://aip.baidubce.com/api/v1/solution/direct/img_censor";
    public   static final String B_TEXTURL = "https://aip.baidubce.com/rest/2.0/antispam/v2/spam";
    public static final String B_USERNAME = "xx";
    public static final String B_PASSWORD = "xx";
    public static final String TOPIC_ID = "xx";
    public static final List<String> WHITE_USER_LIST = new ArrayList<String>(){{add("xx"); add("xx");}};
    /**
     * 政治敏感
     */
    public static final String TYPECODE_POLITICIAN = "10010";
    /**
     * 色情识别
     */
    public static final String TYPECODE_ANTIPORN = "10011";
    /**
     * 暴恐识别
     */
    public static final String TYPECODE_TERROR = "10012";
    /**
     * 恶心图像识别
     */
    public static final String TYPECODE_DISGUST = "10013";
    /**
     * 广告检测
     */
    public static final String TYPECODE_WATERMARK = "10014";

    /**
     * 文字检测
     */
    public static final String TYPECODE_SPAM = "10015";

    public static final String MSG_TiTle = "检测结果";
}
