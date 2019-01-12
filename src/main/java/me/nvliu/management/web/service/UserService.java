package me.nvliu.management.web.service;

import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.User;


public interface UserService {
    /**
     * 通过用户ID获取用户信息
     * @param id 用户id
     * @return
     */
     Result getUserById(Integer id);

    /**
     * 通过用户名获取用户信息
     * @param userName 用户名
     * @return
     */
    User getUserByName(String userName);

    /**
     * 获取用户列表信息
     * @param user 用户列表
     * @return
     */
    Result getUserList(User user);

    /**
     * 获取用户分页信息
     * @param userName 用户名
     * @param pageNumber 页码
     * @param pageSize 每页条数
     * @return
     */
    Result getUserPage(String userName, int pageNumber, int pageSize);

    /**
     * 保存用户信息
     * @param user 用户
     * @return
     */
    Result saveUser(User user);

    /**
     * 删除用户信息
     * @param id 用户id
     * @return
     */
    Result deleteUser(Integer id);

    /**
     * 更新用户信息
     * @param id 用户id
     * @param user 更新的用户信息
     * @return
     */
    Result updadteUser(int id,User user);

    /**
     * 修改当前用户密码
     * @param userName 当前登录用户的用户名
     * @param opwd 旧密码
     * @param npwd 新密码
     * @return
     */
    Result updateUserPassword(String userName,String opwd,String npwd);

    /**
     * 修改用户角色
     * @param id 用户id
     * @param roleIds 角色ID串
     * @return
     */
    Result updateUserRole(int id,String roleIds);



}
