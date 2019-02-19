package me.nvliu.management.web.api;

import me.nvliu.management.web.api.base.BaseController;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/menu")
public class MenuController extends BaseController {

    @Autowired
    MenuService menuService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> saveMenu(Menu menu){
        return menuService.saveMenu(menu);
    }
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.CREATED)
    public Result<Object> updateMenu(Menu menu){
        return menuService.updadteMenu(menu);
    }
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> deleteMenu(@RequestParam(value = "id") Integer id){
        return menuService.deleteMenu(id);
    }
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> getMenuPage(@RequestParam(value = "name" ,required = false) String  name,
                                      @RequestParam(value = "parentId",required = false) Integer parentId,
                                      @RequestParam(value = "pageNum" ,defaultValue = "1") Integer  pageNum,
                                      @RequestParam(value = "pageSize" ,defaultValue = "10") Integer  pageSize){
        return menuService.getMenuPage(name,parentId,pageNum,pageSize);
    }

}
