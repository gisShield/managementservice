package me.nvliu.management.service;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.service
 * @Description: 微博查询服务接口
 * @Date: Create in 14:42 2018/9/27
 * @Modified By:
 */
public interface BlogService {
    /**
     * 通过微博id 获取微博
     * @param id
     * @return
     */
    HashMap<String,Object> findBlogById(String id);

    /**
     * 获取微博分页
     * @param map 查询条件
     * @return
     */
    HashMap<String,Object> findBlogByPage(Map<String,String> map);
}
