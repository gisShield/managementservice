package me.nvliu.management.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.nvliu.management.utils.Tools;
import me.nvliu.management.web.dao.UserMapper;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.User;
import me.nvliu.management.web.entity.UserRole;
import me.nvliu.management.web.service.UserService;
import me.nvliu.management.web.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserMapper userMapper;

    @Override
    public Result getUserById(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        return new Result(user, Result.ErrorCode.SUCCESS_OPTION);
    }

    @Override
    public User getUserByName(String userName) {
        User user =  userMapper.findByUserName(userName);
        return user;

    }

    @Override
    public Result getUserList(User user) {
        if (Tools.notEmpty(user)){
            List<UserVo> list =  userMapper.getUserList(user);
            return new Result(list, Result.ErrorCode.SUCCESS_OPTION);
        }else {
            return new Result(Result.ErrorCode.BAD_REQUEST);
        }

    }

    @Override
    public Result getUserPage(String userName, int pageNumber, int pageSize) {
        PageInfo<UserVo> pageInfo = null;
        int p = 0;
        int s = 10;
        if(Tools.notEmpty(pageNumber)) {
            p = pageNumber;
        }
        if (Tools.notEmpty(pageSize)) {
            s = pageSize;
        }
        User user = new User();
        if(Tools.notEmpty(userName)){
            user.setUserName(userName);
        }
        PageHelper.startPage(p,s);
        pageInfo = new PageInfo<>(userMapper.getUserList(user));
        return new Result(pageInfo, Result.ErrorCode.SUCCESS_OPTION);
    }

    @Override
    public Result saveUser(User user) {
        String pwd = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pwd);
        int res = userMapper.insertSelective(user);
        if(res>0){
            return new Result(Result.ErrorCode.SUCCESS_OPTION);
        }else {
            return new Result(Result.ErrorCode.FAIL_OPTION);
        }



    }

    @Override
    public Result deleteUser(Integer id) {
        if(Tools.notEmpty(id)){
            int res =  userMapper.deleteByPrimaryKey(id);
            if(res >0 ){
                return new Result(Result.ErrorCode.SUCCESS_OPTION);
            }else{
                return new Result(Result.ErrorCode.FAIL_OPTION);
            }
        }else{
            return new Result(Result.ErrorCode.BAD_REQUEST);
        }

    }

    @Override
    public Result updadteUser(int id,User user) {
        if(Tools.notEmpty(user) && Tools.notEmpty(id)){
            user.setId(id);
            int res =  userMapper.updateByPrimaryKeySelective(user);
            if(res >0){
                return new Result(Result.ErrorCode.SUCCESS_OPTION);
            }else{
                return new Result(Result.ErrorCode.FAIL_OPTION);
            }
        }else {
            return new Result(Result.ErrorCode.BAD_REQUEST);
        }

    }

    @Override
    public Result updateUserPassword(String userName,String opwd,String npwd) {
        User u = userMapper.findByUserName(userName);
        if(Tools.notEmpty(u)){
            if(BCrypt.checkpw(opwd,u.getPassword())){
                // 密码验证正确
                String newPwd = BCrypt.hashpw(npwd, BCrypt.gensalt());
                u.setPassword(newPwd);
                int res =  userMapper.updatePasswordByPrimaryKeySelective(u);
                if(res >0){
                    return new Result(Result.ErrorCode.EDITPWD_SUCCESS);
                }else {
                    return new Result(Result.ErrorCode.FAIL_OPTION);
                }
            }else{
                return new Result(Result.ErrorCode.INVALID_PASSWORD);
            }

        }else{
            return new Result(Result.ErrorCode.BAD_REQUEST);
        }
    }

    @Override
    public Result updateUserRole(int id,String roleIds) {
        if(Tools.notEmpty(id) && Tools.notEmpty(roleIds)){
            //清除之前绑定的关联记录
            User user = new User();
            user.setId(id);
            userMapper.removeUserRole(user);
            // 新增用户角色关联
            String[] newRoleIds = roleIds.split(",");

            for(int i=0;i<newRoleIds.length;i++){
                UserRole userRole= new UserRole(user.getId(),Integer.parseInt(newRoleIds[i]));
                userMapper.addUserRole(userRole);
            }
            return new Result(Result.ErrorCode.SUCCESS_OPTION);
        }else{
            return new Result(Result.ErrorCode.BAD_REQUEST);
        }

    }
}
