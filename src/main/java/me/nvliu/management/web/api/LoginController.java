package me.nvliu.management.web.api;

import me.nvliu.management.web.api.base.BaseController;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.User;
import me.nvliu.management.web.service.MenuService;
import me.nvliu.management.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController extends BaseController{
    @Autowired
    UserService userService;
    @Autowired
    MenuService menuService;

    @CrossOrigin(origins = { "*" }, maxAge = 6000)
    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public Object login(@AuthenticationPrincipal User loginedUser) {
        if (loginedUser != null) {
            // 开始获取用户信息，角色以及菜单
            List<Menu> permissions = menuService.getMenuByUserId(loginedUser.getId());
            return new Result<>(permissions,Result.ErrorCode.LOGIN_SUCCESS);
        }
        return null;
    }
    @RequestMapping(value = "/logout" ,method = RequestMethod.POST)
    public Object login() {
        return new Result<>(Result.ErrorCode.LOGOUT_SUCCESS);
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public Object index(){
        return "hello word!";
    }

}
