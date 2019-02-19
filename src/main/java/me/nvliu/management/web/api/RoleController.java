package me.nvliu.management.web.api;

import me.nvliu.management.web.api.base.BaseController;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.Role;
import me.nvliu.management.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * 角色
 * @author mvp
 */
@RestController
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> saveRole(Role role){
        return roleService.saveRole(role);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> updateRole(Role role){
        return roleService.updadteRole(role);
    }
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> deleteRole(@RequestParam(value = "id") Integer id){
        return roleService.deleteRole(id);
    }
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> getRolePage(@RequestParam(value = "roleName",required = false) String  roleName,
                                      @RequestParam(value = "pageNum" ,defaultValue = "1") Integer  pageNum,
                                      @RequestParam(value = "pageSize" ,defaultValue = "10")  Integer  pageSize){
        return roleService.getRolePage(roleName,pageNum,pageSize);
    }
    @PostMapping("/roleMenu")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> addRoleMenu(@RequestParam(value = "id") Integer id,
                                      @RequestParam(value = "menuIds") String menuIds){

        return roleService.updateRoleMenu(id,menuIds);
    }
}
