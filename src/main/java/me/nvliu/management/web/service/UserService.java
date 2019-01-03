package me.nvliu.management.web.service;

import com.github.pagehelper.PageInfo;
import me.nvliu.management.web.entity.User;

import java.util.List;

public interface UserService {
    /**
     * 通过用户ID获取用户信息
     * @param id
     * @return
     */
     User getUserById(Integer id);

    /**
     * 通过用户名获取用户信息
     * @param userName
     * @return
     */
     User getUserByName(String userName);

    /**
     * 获取用户列表信息
     * @param user
     * @return
     */
     List<User> getUserList(User user);

    /**
     * 获取用户分页信息
     * @param user
     * @param pageNumber
     * @param pageSize
     * @return
     */
     PageInfo<User> getUserPage(User user, int pageNumber, int pageSize);

    /**
     * 保存用户信息
     * @param user
     * @return
     */
     int saveUser(User user);

    /**
     * 删除用户信息
     * @param id
     * @return
     */
     int deleteUser(Integer id);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
     int updadteUser(User user);

    /**
     * 修改用户角色
     * @param user
     * @return
     */
     int updateUserRole(User user,String roleIds);



}
