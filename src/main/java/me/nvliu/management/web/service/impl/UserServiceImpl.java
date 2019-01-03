package me.nvliu.management.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.nvliu.management.utils.Tools;
import me.nvliu.management.web.dao.UserMapper;
import me.nvliu.management.web.entity.User;
import me.nvliu.management.web.entity.UserRole;
import me.nvliu.management.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserMapper userMapper;

    @Override
    public User getUserById(Integer id) {
        User user= null;
        if(Tools.notEmpty(id)){
            user = userMapper.selectByPrimaryKey(id);
        }
        return user;
    }

    @Override
    public User getUserByName(String userName) {
        if(Tools.isEmpty(userName)){
            return null;
        }
        return userMapper.findByUserName(userName);

    }

    @Override
    public List<User> getUserList(User user) {
        List<User> list = null;
        if (Tools.notEmpty(user)){
            list =  userMapper.getUserList(user);
        }
        return list;
    }

    @Override
    public PageInfo<User> getUserPage(User user, int pageNumber, int pageSize) {
        PageInfo<User> pageInfo = null;
        int p = 0;
        int s = 10;
        if(Tools.notEmpty(pageNumber)) {
            p = pageNumber;
        }
        if (Tools.notEmpty(pageSize)) {
            s = pageSize;
        }
        if (Tools.notEmpty(user)){
            PageHelper.startPage(p,s);
            pageInfo = new PageInfo<>(userMapper.getUserList(user));
        }
        return pageInfo;
    }

    @Override
    public int saveUser(User user) {

        return userMapper.insertSelective(user);
    }

    @Override
    public int deleteUser(Integer id) {
        if(Tools.notEmpty(id)){
            return userMapper.deleteByPrimaryKey(id);
        }else{
            return -1;
        }

    }

    @Override
    public int updadteUser(User user) {
        if(Tools.notEmpty(user) && Tools.notEmpty(user.getId())){
            return userMapper.updateByPrimaryKeySelective(user);
        }else {
            return -1;
        }

    }

    @Override
    public int updateUserRole(User user,String roleIds) {
        int j = 0;
        if(Tools.notEmpty(user) && Tools.notEmpty(roleIds)){
            //清除之前绑定的关联记录
            userMapper.removeUserRole(user);
            // 新增用户角色关联
            String[] newRoleIds = roleIds.split(",");

            for(int i=0;i<newRoleIds.length;i++){
                UserRole userRole= new UserRole(user.getId(),Integer.parseInt(newRoleIds[i]));
                userMapper.addUserRole(userRole);
                j++;
            }
        }
        return j;
    }
}
