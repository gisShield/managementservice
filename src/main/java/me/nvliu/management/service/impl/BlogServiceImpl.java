package me.nvliu.management.service.impl;

import com.alibaba.fastjson.JSONObject;
import me.nvliu.management.auditing.BlogLogin;
import me.nvliu.management.constants.UtilConstants;
import me.nvliu.management.entity.ReturnModel;
import me.nvliu.management.repository.BlogRepository;
import me.nvliu.management.service.BlogService;
import me.nvliu.management.utils.DateUtil;
import me.nvliu.management.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.service.impl
 * @Description: 微博服务查询实现类
 * @Date: Create in 14:49 2018/9/27
 * @Modified By:
 */
@Service
public class BlogServiceImpl implements BlogService {

    private final static Logger log = LoggerFactory.getLogger(BlogServiceImpl.class);


    private final String STR_PAGE = "page";
    private final String STR_ROWS = "rows";
    private final String STR_BLOG_USER_NAME = "blogUserName";
    private final String STR_TIME_BEGIN = "timeBegin";
    private final String STR_TIME_END = "timeEnd";
    private final String STR_BLOCK = "isBlock";

    @Autowired
    private BlogRepository blogRepository;

    private BlogLogin blogLogin;

    /**
     * 通过微博id 获取微博
     *
     * @param id
     * @return
     */
    @Override
    public HashMap<String, Object> findBlogById(String id) {
        if(Tools.notEmpty(id)){
            return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,blogRepository.findByBlogId(id));
        }
        return null;
    }

    /**
     * 获取微博分页
     *
     * @param map 查询条件
     * @return
     */
    @Override
    public HashMap<String, Object> findBlogByPage(Map<String,String> map) {
        int page =1;
        int rows = 10;
        if(Tools.notEmpty(map,STR_PAGE)){
            page = Integer.parseInt(map.get(STR_PAGE));
        }
        if(Tools.notEmpty(map,STR_ROWS)){
            rows = Integer.parseInt(map.get(STR_ROWS));
        }
        PageRequest pageRequest =PageRequest.of(page-1,rows);
        if(Tools.notEmpty(map,STR_BLOG_USER_NAME)){
            if(Tools.notEmpty(map,STR_TIME_BEGIN) && Tools.notEmpty(map,STR_TIME_END)){
                try {
                    Date timeBegin = DateUtil.formatDate(map.get(STR_TIME_BEGIN),UtilConstants.FORMATDATE_Y_M_D_T);
                    Date timeEnd = DateUtil.formatDate(map.get(STR_TIME_END),UtilConstants.FORMATDATE_Y_M_D_T);

                    return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,blogRepository.findAllByBlogTimeBetweenAndBlogUserNameLikeOrderByBlogTimeDesc(timeBegin,timeEnd,map.get("blogUserName"),pageRequest).getContent());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,blogRepository.findAllByBlogUserNameLike(map.get(STR_BLOG_USER_NAME),pageRequest).getContent());
        }else{
            if(Tools.notEmpty(map,STR_TIME_BEGIN) && Tools.notEmpty(map,STR_TIME_END)){
                try {
                    Date timeBegin = DateUtil.formatDate(map.get(STR_TIME_BEGIN),UtilConstants.FORMATDATE_Y_M_D_T);
                    Date timeEnd = DateUtil.formatDate(map.get(STR_TIME_END),UtilConstants.FORMATDATE_Y_M_D_T);
                    return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,blogRepository.findAllByBlogTimeBetween(timeBegin,timeEnd,pageRequest).getContent());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return ReturnModel.getModel(UtilConstants.MSG_QUERY_F,UtilConstants.CODE_FAILED,null);
    }

    @Override
    public HashMap<String, Object> findBlockBlogByPage(Map<String, String> map) {
        int page =1;
        int rows = 10;
        if(Tools.notEmpty(map,STR_PAGE)){
            page = Integer.parseInt(map.get(STR_PAGE));
        }
        if(Tools.notEmpty(map,STR_ROWS)){
            rows = Integer.parseInt(map.get(STR_ROWS));
        }
        PageRequest pageRequest =PageRequest.of(page-1,rows);
        if(Tools.notEmpty(map,STR_BLOCK)){
            if(Tools.notEmpty(map,STR_TIME_BEGIN) && Tools.notEmpty(map,STR_TIME_END)){
                // 查询指定时间内封禁的微博分页
                try {
                    Date timeBegin = DateUtil.formatDate(map.get(STR_TIME_BEGIN),UtilConstants.FORMATDATE_Y_M_D_T);
                    Date timeEnd = DateUtil.formatDate(map.get(STR_TIME_END),UtilConstants.FORMATDATE_Y_M_D_T);
                    return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,blogRepository.findAllByIsBlockAndBlogTimeBetween(map.get(STR_BLOCK),timeBegin,timeEnd,pageRequest).getContent());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,blogRepository.findAllByIsBlock(map.get(STR_BLOCK),pageRequest).getContent());
        }else{
            // 查询所有封禁的微博分页
            return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,null);
        }
    }

    @Override
    public HashMap<String, Object> preLoginBlog(Map<String, String> map) {
        if(Tools.notEmpty(map,"userName")){
            blogLogin = new BlogLogin();
            blogLogin.setUserName(map.get("userName"));
            JSONObject jsonObject = blogLogin.getPreLoginData();
            blogLogin.setPreData(jsonObject);
            if(jsonObject.containsKey("showpin") && jsonObject.getIntValue("showpin") == 1){
                String  url = "https://login.sina.com.cn/cgi/pin.php?&s=0&p="+jsonObject.getString("pcid");
                jsonObject.put("pinurl",url);
                return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,jsonObject);
            }
        }
        return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,null);
    }
    @Override
    public HashMap<String, Object> getBlogPin(Map<String, String> map) {
        String url = "https://login.sina.com.cn/cgi/pin.php?&s=0&r="+Math.floor(Math.random()*1e8)+"&p="+map.get("pcid");
        return ReturnModel.getModel(UtilConstants.MSG_QUERY_S,UtilConstants.CODE_SUCCESS,url);
    }

    @Override
    public HashMap<String, Object> loginBlog(Map<String, String> map) {
        if(blogLogin!= null){
            blogLogin.setPwd(map.get("pwd"));
            blogLogin.getPreData().put("door",map.get("pin"));
            blogLogin.login();
//            LoginHttpClient.printCookies();
            blogLogin.blockBlog(map.get("mid"));
        }
        return null;
    }
}
