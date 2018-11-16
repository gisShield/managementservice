package me.nvliu.management.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.auditing.BaiduApi;
import me.nvliu.management.entity.Blog;
import me.nvliu.management.entity.TieBa;
import me.nvliu.management.entity.YuBa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.utils
 * @Description:
 * @Date: Create in 15:13 2018/9/25
 * @Modified By:
 */
public class TagUtils {
    private final static Logger log = LoggerFactory.getLogger(TagUtils.class);

    public static List<String> setTags(Blog blog, BaiduApi baiduApi) {
        List<String> list = new ArrayList();
        if (Tools.notEmpty(blog.getBlogText())) {
            String textRes = getTextRes(blog.getBlogText(), baiduApi);
            list.add(textRes);
        }
        if (blog.getBlogImages() != null && (blog.getBlogImages().size() > 0)) {
            List<String> imgs = blog.getBlogImages();
            for (String item : imgs
                    ) {
                if (item.endsWith(".jpg") || item.endsWith(".png")) {
                    String imageRes = getImageRes(item, baiduApi);
                    list.add(imageRes);
                } else {
                    list.add("");
                }
            }
        }
        return list;
    }

    public static void setTags(TieBa tieBa) {

    }

    public static void setTags(YuBa yuBa) {

    }

    private static String getTextRes(String str, BaiduApi api) {
        String res = api.getTextAuditing(str);
        log.info(res);
        JSONObject j = new JSONObject();
        if (res != null) {
            JSONObject jsonObject = JSONObject.parseObject(res);
            if (jsonObject.containsKey("error_code")) {
                //报错信息直接回到上层进行记录
                return res;
            }
            JSONObject resjson = jsonObject.getJSONObject("result");
            if (resjson != null) {
                j.put("spam", resjson.getIntValue("spam"));
                switch (resjson.getIntValue("spam")) {
                    case 1:
                        // 违禁关键词
                        JSONArray jsonArray = resjson.getJSONArray("reject");
                        j.put("reject", jsonArray);
                        break;
                    case 2:
                        // 人工待审核关键词
                        JSONArray jsonArray1 = resjson.getJSONArray("review");
                        j.put("review", jsonArray1);
                        break;
                    default:
                        break;
                }
            }
            return j.toJSONString();
        }
        return j.toJSONString();
    }

    private static String getImageRes(String str, BaiduApi api) {
        String res = api.getAuditing(str);
        log.info(res);
        JSONObject imageRes = new JSONObject();
        if (res != null) {
            JSONObject jsonObject = JSONObject.parseObject(res).getJSONObject("result");
            // ocr
            JSONObject ocrJson = jsonObject.getJSONObject("ocr");
            if (ocrJson != null) {
                if (ocrJson.containsKey("error_code")) {
                    return null;
                } else if (ocrJson.getIntValue("words_result_num") > 0) {
                    JSONArray jsonArray = ocrJson.getJSONArray("words_result");
                    StringBuffer stringBuffer = new StringBuffer();
                    jsonArray.forEach(item -> {
                        JSONObject jsonObject1 = (JSONObject) item;
                        stringBuffer.append(jsonObject1.getString("words"));
                    });
                    imageRes.put("ocr", stringBuffer.toString());
                } else {
                    imageRes.put("ocr", null);
                }
            }
            //politician
            JSONObject politicianJson = jsonObject.getJSONObject("politician");
            if (politicianJson != null) {
                if (politicianJson.containsKey("error_code")) {
                    return null;
                } else if ("是".equals(politicianJson.getString("include_politician"))) {
                    List names = new ArrayList();
                    JSONArray result = politicianJson.getJSONArray("result");
                    result.forEach(item -> {
                        JSONObject itemJson = (JSONObject) item;
                        JSONArray stars = itemJson.getJSONArray("stars");
                        stars.forEach(j -> {
                            JSONObject jjson = (JSONObject) j;
                            names.add(jjson.getString("name"));
                        });
                    });
                    imageRes.put("politician", names);
                }
            }
            //antiporn
            JSONObject antipornJson = jsonObject.getJSONObject("antiporn");
            if (antipornJson != null) {
                if (antipornJson.containsKey("error_code")) {
                    return null;
                } else if ("色情".equals(antipornJson.getString("conclusion")) || "性感".equals(antipornJson.getString("conclusion"))) {
                    imageRes.put("conclusion", 1);
                } else {
                    imageRes.put("conclusion", 0);
                }
            }
            //terror
            JSONObject terrorJson = jsonObject.getJSONObject("terror");
            if (terrorJson != null) {
                if (terrorJson.containsKey("error_code")) {
                    return null;
                } else if (terrorJson.getDouble("result") > 0.5) {
                    imageRes.put("terror", terrorJson.getDouble("result"));
                } else {
                    imageRes.put("terror", 0);
                }
            }
            //disgust
            JSONObject disgustJson = jsonObject.getJSONObject("disgust");
            if (disgustJson != null) {
                if (disgustJson.containsKey("error_code")) {
                    return null;
                } else if (disgustJson.getDouble("result") > 0.5) {
                    imageRes.put("disgust", disgustJson.getDouble("result"));
                } else {
                    imageRes.put("disgust", 0);
                }
            }
            //watermark
            JSONObject watermarkJson = jsonObject.getJSONObject("watermark");
            if (watermarkJson != null) {
                if (watermarkJson.containsKey("error_code")) {
                    return null;
                } else if (watermarkJson.getIntValue("result_num") > 0) {
                    JSONObject res1 = new JSONObject();
                    JSONArray jsonArray = watermarkJson.getJSONArray("result");
                    StringBuffer stringBuffer = new StringBuffer();
                    jsonArray.forEach(item -> {
                        JSONObject jsonObject1 = (JSONObject) item;
                        if (jsonObject1.getDouble("probability") > 0.5) {
                            if ("QR code".equals(jsonObject1.getString("type"))) {
                                stringBuffer.append("qr;");
                            }
                            if ("bar code".equals(jsonObject1.getString("type"))) {
                                stringBuffer.append("bar;");
                            }
                        }
                    });
                    imageRes.put("watermark", stringBuffer);
                } else {
                    imageRes.put("watermark", null);
                }
            }
        }
        return imageRes.toJSONString();
    }
}