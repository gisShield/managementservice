package me.nvliu.management.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.nvliu.management.utils.Tools;
import me.nvliu.management.web.dao.RoleMapper;
import me.nvliu.management.web.entity.Role;
import me.nvliu.management.web.entity.RoleMenu;
import me.nvliu.management.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleMapper roleMapper;

    @Override
    public Role getRoleById(Integer id) {
        Role role = null;
        if(Tools.notEmpty(id)){
            role = roleMapper.selectByPrimaryKey(id);
        }
        return role;
    }

    @Override
    public List<Role> getRoleList(Role role) {
        List<Role> roles = null;
        if(Tools.notEmpty(role)){
            roles = roleMapper.getRoleList(role);
        }
        return roles;
    }

    @Override
    public PageInfo<Role> getRolePage(Role role, int pageNumber, int pageSize) {
        int p = 1;
        int s = 10;
        PageInfo<Role> rolePageInfo = null;
        if(Tools.notEmpty(pageNumber)){
            p = pageNumber;

        }
        if(Tools.notEmpty(pageSize)){
            s = pageSize;

        }
        if(Tools.notEmpty(role)){
            PageHelper.startPage(p,s);
            rolePageInfo = new PageInfo<>(roleMapper.getRoleList(role));
        }
        return rolePageInfo;
    }

    @Override
    public int saveRole(Role role) {
        return roleMapper.insertSelective(role);
    }

    @Override
    public int deleteRole(Integer id) {
        return roleMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updadteRole(Role role) {
        if(Tools.notEmpty(role) && Tools.notEmpty(role.getId())){
            return  roleMapper.updateByPrimaryKeySelective(role);
        }else{
            return -1;
        }
    }

    @Override
    public int updateRoleMenu(Role role, String menuIds) {
        int j = 0;
        if(Tools.notEmpty(role) && Tools.notEmpty(menuIds)){
            //清除之前绑定的关联记录
            roleMapper.removeRoleMenu(role);
            // 新增角色菜单关联
            String[] newMenuIds = menuIds.split(",");

            for(int i=0;i<newMenuIds.length;i++){
                RoleMenu roleMenu= new RoleMenu(role.getId(),Integer.parseInt(newMenuIds[i]));
                roleMapper.addRoleMenu(roleMenu);
                j++;
            }
        }
        return j;
    }
}
