package me.nvliu.management.web.api;

import me.nvliu.management.web.api.base.BaseController;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.entity.MenuTree;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.User;
import me.nvliu.management.web.service.MenuService;
import me.nvliu.management.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/** 用户视图
 * @author mvp
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    private MenuService menuService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
   public Result<Object> saveUser(User user){
        return userService.saveUser(user);
   }

   @PutMapping("/update")
   @ResponseStatus(HttpStatus.CREATED)
   public Result<Object> updateUser(User user){
       return userService.updadteUser(user);
   }
    @PutMapping("/updatepwd")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> updateUserPassword(@RequestParam(value = "opwd") String opwd,
                                             @RequestParam(value = "npwd") String npwd){
        return userService.updateUserPassword(this.getSessionUserName(),opwd,npwd);
    }
   @PostMapping("/remove")
   @ResponseStatus(HttpStatus.OK)
   public Result<Object> deleteUser(@RequestParam Integer id){
       return userService.deleteUser(id);
   }

   @GetMapping("/page")
   @ResponseStatus(HttpStatus.OK)
   public Result<Object> getUserPage(@RequestParam(value = "userName" ,required = false) String userName,
                                     @RequestParam(value =  "pageNum" ,defaultValue = "1") Integer pageNum,
                                     @RequestParam(value =  "pageSize" ,defaultValue = "10") Integer pageSize){
       return userService.getUserPage(userName,pageNum,pageSize);
   }
   @PostMapping("/userRole")
   @ResponseStatus(HttpStatus.CREATED)
   public Result<Object> addUserRole(@RequestParam( value =  "id") Integer id,
                                     @RequestParam(value = "roleIds") String roleIds){
       return  userService.updateUserRole(id,roleIds);
   }
    @GetMapping("/userMenu")
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> getUserMenu(@RequestParam(value = "id") Integer id){
        List<Menu> permissions = menuService.getMenuByUserId(id);
        MenuTree menuTree = new MenuTree();
        return new Result<>(menuTree.menuList(permissions),Result.ErrorCode.SUCCESS_OPTION);
    }
}
