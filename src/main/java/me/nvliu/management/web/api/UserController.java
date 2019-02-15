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
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
   public Result<Object> saveUser(@Valid @RequestBody User user, Errors errors){
        List<ObjectError> oes = errors.getAllErrors();
        if(oes.size() == 0){
            return userService.saveUser(user);
        }
        return new Result<>(Result.ErrorCode.BAD_REQUEST);
   }
   @PutMapping("/update")
   @ResponseStatus(HttpStatus.CREATED)
   public Result<Object> updateUser(@PathVariable("id") Integer id,@Valid @RequestBody User user,Errors errors){
       List<ObjectError> oes = errors.getAllErrors();
       if(oes.size() == 0 ){
           return userService.updadteUser(id,user);
       }
       return new Result<>(Result.ErrorCode.BAD_REQUEST);
   }
    @PutMapping("/updatepwd")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> updateUserPassword(@PathVariable("opwd") String opwd,@PathVariable("npwd") String npwd,Errors errors){
        List<ObjectError> oes = errors.getAllErrors();
        if(oes.size() == 0 ){
            return userService.updateUserPassword(this.getSessionUserName(),opwd,npwd);
        }
        return new Result<>(Result.ErrorCode.BAD_REQUEST);
    }
   @DeleteMapping("/remove")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public Result<Object> deleteUser(@PathVariable("id") Integer id){
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
//       return  userService.updateUserRole(id,roleIds);
       System.out.println(id);
       System.out.println(roleIds);
       return new Result<>(Result.ErrorCode.SUCCESS_OPTION);
   }
    @GetMapping("/userMenu")
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> getUserMenu(@RequestParam(value = "id") Integer id){
        List<Menu> permissions = menuService.getMenuByUserId(id);
        MenuTree menuTree = new MenuTree();
        return new Result<>(menuTree.menuList(permissions),Result.ErrorCode.SUCCESS_OPTION);
    }



}
