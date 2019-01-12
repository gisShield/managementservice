package me.nvliu.management.web.api;

import com.github.pagehelper.PageInfo;
import me.nvliu.management.web.api.base.BaseController;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.User;
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
public class UserController extends BaseController {
    @Autowired
    UserService userService;
    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
   public Result<Object> saveUser(@Valid @RequestBody User user, Errors errors){
        List<ObjectError> oes = errors.getAllErrors();
        if(oes.size() == 0){
            return userService.saveUser(user);
        }
        return new Result<>(Result.ErrorCode.BAD_REQUEST);
   }
   @PutMapping("/user/{id}")
   @ResponseStatus(HttpStatus.CREATED)
   public Result<Object> updateUser(@PathVariable("id") Integer id,@Valid @RequestBody User user,Errors errors){
       List<ObjectError> oes = errors.getAllErrors();
       if(oes.size() == 0 ){
           return userService.updadteUser(id,user);
       }
       return new Result<>(Result.ErrorCode.BAD_REQUEST);
   }
    @PutMapping("/user/{opwd}/{npwd}")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> updateUserPassword(@PathVariable("opwd") String opwd,@PathVariable("npwd") String npwd,Errors errors){
        List<ObjectError> oes = errors.getAllErrors();
        if(oes.size() == 0 ){
            return userService.updateUserPassword(this.getSessionUserName(),opwd,npwd);
        }
        return new Result<>(Result.ErrorCode.BAD_REQUEST);
    }
   @DeleteMapping("/user/{id}")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public Result<Object> deleteUser(@PathVariable("id") Integer id){
       return userService.deleteUser(id);
   }
   @GetMapping("/user/{userName}/{pageNum}/{pageSize}")
   @ResponseStatus(HttpStatus.OK)
   public Result<Object> getUserPage(@PathVariable("userName") String userName,@PathVariable("pageNum") Integer pageNum,@PathVariable("pageSize") Integer pageSize){
       return userService.getUserPage(userName,pageNum,pageSize);
   }
   @PostMapping("/user/role/{id}/{roleIds}")
   @ResponseStatus(HttpStatus.CREATED)
   public Result<Object> addUserRole(@PathVariable("id") Integer id, @PathVariable("roleIds") String roleIds){
       return  userService.updateUserRole(id,roleIds);
   }


}
