package me.nvliu.management.web.api;

import me.nvliu.management.web.api.base.BaseController;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.entity.Role;
import me.nvliu.management.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class MenuController extends BaseController {

    @Autowired
    MenuService menuService;

    @PostMapping("/menu")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> saveMenu(@Valid @RequestBody Menu menu, Errors errors){
        return null;
    }
    @PutMapping("/menu/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> updateMenu(@PathVariable Integer id, @RequestBody Menu menu, Errors errors){
        return null;
    }
    @DeleteMapping("/menu/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Result<Object> deleteMenu(@PathVariable Integer id){
        return null;
    }
    @GetMapping("/menu/{pageNum}/{pageSize}")
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> getMenuPage(@RequestBody Menu  menu,@PathVariable Integer  pageNum,@PathVariable Integer  pageSize){
        return null;
    }

}
