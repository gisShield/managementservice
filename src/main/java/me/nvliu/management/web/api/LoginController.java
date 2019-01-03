package me.nvliu.management.web.api;

import me.nvliu.management.web.entity.User;
import me.nvliu.management.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    UserService userService;


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Object login(@AuthenticationPrincipal User loginedUser) {
        if (loginedUser != null) {
            // 开始获取用户信息，角色以及菜单
            return userService.getUserById(loginedUser.getId());
        }
        return null;
    }
    @RequestMapping(value = "/logout" ,method = RequestMethod.POST)
    public Object login() {
        return null;
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public Object index(){
        return "hello word!";
    }

}
