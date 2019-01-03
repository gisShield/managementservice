package me.nvliu.management.web.service;

import com.github.pagehelper.PageInfo;
import me.nvliu.management.web.entity.Role;

import java.util.List;

public interface RoleService {
    /**
     * 通过角色ID获取用角色信息
     * @param id
     * @return
     */
    Role getRoleById(Integer id);


    /**
     * 获取角色列表信息
     * @param role
     * @return
     */
    List<Role> getRoleList(Role role);

    /**
     * 获取角色分页信息
     * @param role
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageInfo<Role> getRolePage(Role role, int pageNumber, int pageSize);

    /**
     * 保存角色信息
     * @param role
     * @return
     */
    int saveRole(Role role);

    /**
     * 删除角色信息
     * @param id
     * @return
     */
    int deleteRole(Integer id);

    /**
     * 更新角色信息
     * @param role
     * @return
     */
    int updadteRole(Role role);

    /**
     * 修改角色菜单
     * @param role
     * @return
     */
    int updateRoleMenu(Role role,String menuIds);
}
