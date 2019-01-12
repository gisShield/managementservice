package me.nvliu.management.web.api;

import me.nvliu.management.web.api.base.BaseController;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.Role;
import me.nvliu.management.web.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色
 * @author mvp
 */
@RestController
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/role")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> saveRole(@Valid @RequestBody Role role, Errors errors){
        List<ObjectError> oes = errors.getAllErrors();
        if(oes.size() == 0 ){
            return roleService.saveRole(role);
        }
        return new Result<>(Result.ErrorCode.BAD_REQUEST);
    }
    @PutMapping("/role/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> updateRole(@PathVariable Integer id, @RequestBody Role role, Errors errors){
        List<ObjectError> oes = errors.getAllErrors();
        if(oes.size()  == 0 ){

            return roleService.updadteRole(id,role);
        }
        return new Result<>(Result.ErrorCode.BAD_REQUEST);
    }
    @DeleteMapping("/role/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Result<Object> deleteRole(@PathVariable Integer id){
        return roleService.deleteRole(id);
    }
    @GetMapping("/role/{roleName}/{pageNum}/{pageSize}")
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> getRolePage(@PathVariable String  roleName,@PathVariable Integer  pageNum,@PathVariable Integer  pageSize){
        return roleService.getRolePage(roleName,pageNum,pageSize);
    }
    @PostMapping("/role/menu/{id}/{menuIds}")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> addRoleMenu(@PathVariable Integer id,@PathVariable String menuIds){

        return roleService.updateRoleMenu(id,menuIds);
    }
}
