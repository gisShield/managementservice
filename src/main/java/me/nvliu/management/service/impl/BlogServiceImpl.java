package me.nvliu.management.service.impl;

import me.nvliu.management.constants.UtilConstants;
import me.nvliu.management.entity.ReturnModel;
import me.nvliu.management.repository.BlogRepository;
import me.nvliu.management.service.BlogService;
import me.nvliu.management.utils.DateUtil;
import me.nvliu.management.utils.Tools;
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
    @Autowired
    private BlogRepository blogRepository;

    /**
     * 通过微博id 获取微博
     *
     * @param id
     * @return
     */
    @Override
    public HashMap<String, Object> findBlogById(String id) {
        if(Tools.notEmpty(id)){
            return ReturnModel.getModel("查询成功","sucess",blogRepository.findByBlogId(id));
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
        if(map.containsKey("page") && Tools.notEmpty(map.get("page"))){
            page = Integer.parseInt(map.get("page"));
        }
        if(map.containsKey("rows") && Tools.notEmpty(map.get("rows"))){
            rows = Integer.parseInt(map.get("rows"));
        }
        PageRequest pageRequest =PageRequest.of(page-1,rows);
        if(map.containsKey("blogUserName") && Tools.notEmpty(map.get("blogUserName"))){
            if(map.containsKey("timeBegin") && map.containsKey("timeEnd")){
                try {
                    Date timeBegin = DateUtil.formatDate(map.get("timeBegin"),UtilConstants.FORMATDATE_Y_M_D_T);
                    Date timeEnd = DateUtil.formatDate(map.get("timeEnd"),UtilConstants.FORMATDATE_Y_M_D_T);

                    return ReturnModel.getModel("查询成功","sucess",blogRepository.findAllByBlogTimeBetweenAndBlogUserNameLikeOrderByBlogTimeDesc(timeBegin,timeEnd,map.get("blogUserName"),pageRequest).getContent());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return ReturnModel.getModel("查询成功","sucess",blogRepository.findAllByBlogUserNameLike(map.get("blogUserName"),pageRequest).getContent());
        }else{
            if(map.containsKey("timeBegin") && map.containsKey("timeEnd")){
                try {
                    Date timeBegin = DateUtil.formatDate(map.get("timeBegin"),UtilConstants.FORMATDATE_Y_M_D_T);
                    Date timeEnd = DateUtil.formatDate(map.get("timeEnd"),UtilConstants.FORMATDATE_Y_M_D_T);
                    return ReturnModel.getModel("查询成功","sucess",blogRepository.findAllByBlogTimeBetween(timeBegin,timeEnd,pageRequest).getContent());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return ReturnModel.getModel("查询失败","sucess",null);
    }
}
