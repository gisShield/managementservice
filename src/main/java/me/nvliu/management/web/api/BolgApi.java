package me.nvliu.management.web.api;

import me.nvliu.management.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.web.api
 * @Description:
 * @Date: Create in 14:39 2018/9/27
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/api/blog")
@CrossOrigin
public class BolgApi {
    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public Object findByUserName(@RequestParam Map map) {
        return blogService.findBlogByPage(map);
    }
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public Object findBlog(@RequestParam(value = "blogId", required = true) String blogId) {
        return blogService.findBlogById(blogId);
    }
    @RequestMapping(value = "/block", method = RequestMethod.GET)
    public Object findBlockBlog(@RequestParam Map map) {
        return blogService.findBlockBlogByPage(map);
    }

    /**
     * 获取登录前提参数
     * @param map
     * @return
     */
    @RequestMapping(value = "/preLogin", method = RequestMethod.POST)
    public Object preLoginBlog(@RequestParam Map map) {
        return blogService.preLoginBlog(map);
    }

    /**
     * 获取验证码
     * @param map
     * @return
     */
    @RequestMapping(value = "/getLoginPin", method = RequestMethod.GET)
    public Object getLoginPin(@RequestParam Map map) {
        return blogService.getBlogPin(map);
    }

    /**
     * 登录微博保存cookie
     * @param map
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object loginBolg(@RequestParam Map map) {
        return blogService.loginBlog(map);
    }
}

