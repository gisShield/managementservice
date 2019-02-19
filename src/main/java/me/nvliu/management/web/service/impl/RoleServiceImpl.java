package me.nvliu.management.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.nvliu.management.utils.Tools;
import me.nvliu.management.web.dao.RoleMapper;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.Role;
import me.nvliu.management.web.entity.RoleMenu;
import me.nvliu.management.web.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleMapper roleMapper;

    @Override
    public Result getRoleById(Integer id) {
        Role role = roleMapper.selectByPrimaryKey(id);
        if(Tools.notEmpty(role)){
            return new Result(role, Result.ErrorCode.SUCCESS_OPTION);
        }else{
            return new Result( Result.ErrorCode.FAIL_OPTION);
        }

    }

    @Override
    public Result getRoleList(Role role) {
        List<Role> roles = roleMapper.getRoleList(role);
        if(!roles.isEmpty()){
            return new Result(roles, Result.ErrorCode.SUCCESS_OPTION);
        }else{
            return new Result( Result.ErrorCode.FAIL_OPTION);
        }
    }

    @Override
    public Result getRolePage(String roleName, int pageNumber, int pageSize) {
        Role role = new Role();
        if(Tools.notEmpty(roleName)){
            role.setRoleName(roleName);
        }
        PageHelper.startPage(pageNumber,pageSize);
        PageInfo<Role> rolePageInfo = new PageInfo<>(roleMapper.getRoleList(role));
        return new Result(rolePageInfo, Result.ErrorCode.SUCCESS_OPTION);
    }

    @Override
    public Result saveRole(Role role) {
        if (StringUtils.isBlank(role.getRoleName())) return new Result( Result.ErrorCode.FAIL_OPTION);
        int res =  roleMapper.insertSelective(role);
        if(res >0){
            return new Result(Result.ErrorCode.SUCCESS_OPTION);
        }else {
            return new Result( Result.ErrorCode.FAIL_OPTION);

        }

    }

    @Override
    public Result deleteRole(Integer id) {
        int res =  roleMapper.deleteByPrimaryKey(id);
        if(res >0){
            return new Result(Result.ErrorCode.SUCCESS_OPTION);
        }else {
            return new Result( Result.ErrorCode.FAIL_OPTION);
        }
    }

    @Override
    public Result updadteRole(Role role) {
        if(Tools.notEmpty(role) && Tools.notEmpty(role.getId())){
            int res =  roleMapper.updateByPrimaryKeySelective(role);
            if(res >0){
                return new Result(Result.ErrorCode.SUCCESS_OPTION);
            }else {
                return new Result( Result.ErrorCode.FAIL_OPTION);
            }
        }else{
            return new Result( Result.ErrorCode.BAD_REQUEST);
        }
    }

    @Override
    public Result updateRoleMenu(int id, String menuIds) {
        if(Tools.notEmpty(id) && Tools.notEmpty(menuIds)){
            //清除之前绑定的关联记录
            Role role = new Role();
            role.setId(id);
            roleMapper.removeRoleMenu(role);
            // 新增角色菜单关联
            String[] newMenuIds = menuIds.split(",");

            for(int i=0;i<newMenuIds.length;i++){
                RoleMenu roleMenu= new RoleMenu(role.getId(),Integer.parseInt(newMenuIds[i]));
                roleMapper.addRoleMenu(roleMenu);
            }
            return new Result(Result.ErrorCode.SUCCESS_OPTION);
        }
        return new Result( Result.ErrorCode.BAD_REQUEST);
    }
}
