package me.nvliu.management.api;

import me.nvliu.management.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.api
 * @Description:
 * @Date: Create in 14:39 2018/9/27
 * @Modified By:
 */
@RestController
@RequestMapping(value = "/api/blog")
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

}

