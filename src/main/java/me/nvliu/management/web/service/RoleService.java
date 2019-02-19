package me.nvliu.management.web.service;

import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.Role;


public interface RoleService {
    /**
     * 通过角色ID获取用角色信息
     * @param id
     * @return
     */
    Result getRoleById(Integer id);


    /**
     * 获取角色列表信息
     * @param role
     * @return
     */
    Result getRoleList(Role role);

    /**
     * 获取角色分页信息
     * @param roleName 角色名
     * @param pageNumber 当前页码
     * @param pageSize 页面条数
     * @return
     */
    Result getRolePage(String roleName, int pageNumber, int pageSize);

    /**
     * 保存角色信息
     * @param role 角色内容
     * @return
     */
    Result saveRole(Role role);

    /**
     * 删除角色信息
     * @param id 角色id
     * @return
     */
    Result deleteRole(Integer id);

    /**
     * 更新角色信息
     * @param id 角色id
     * @param role 角色新的信息
     * @return
     */
    Result updadteRole(Role role);

    /**
     * 修改角色菜单
     * @param id 角色 id
     * @param menuIds 菜单id串
     * @return
     */
    Result updateRoleMenu(int id,String menuIds);
}
