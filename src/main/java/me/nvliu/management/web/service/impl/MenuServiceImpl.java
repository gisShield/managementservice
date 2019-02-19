package me.nvliu.management.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.nvliu.management.utils.Tools;
import me.nvliu.management.web.dao.MenuMapper;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.entity.Result;
import me.nvliu.management.web.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Result getMenuById(Integer id) {
        Menu menu = menuMapper.selectByPrimaryKey(id);
        if(Tools.notEmpty(menu)){
            return new Result(menu, Result.ErrorCode.SUCCESS_OPTION);
        }
        return new Result( Result.ErrorCode.FAIL_OPTION);
    }

    @Override
    public Result getMenuList(Menu menu) {
        List<Menu> menus = menuMapper.getMenuList(menu);
        if(!menus.isEmpty()){
            return new Result(menus, Result.ErrorCode.SUCCESS_OPTION);
        }
        return new Result( Result.ErrorCode.FAIL_OPTION);
    }

    @Override
    public Result getMenuPage(String name,Integer parentId, int pageNumber, int pageSize) {
        Menu menu = new Menu();
        if(StringUtils.isNotBlank(name)){
            menu.setName(name);
        }
        if(parentId != null){
            menu.setParentId(parentId);
        }
        PageHelper.startPage(pageNumber,pageSize);
        PageInfo<Menu> menuPageInfo = new PageInfo<>(menuMapper.getMenuList(menu));

        return new Result(menuPageInfo, Result.ErrorCode.SUCCESS_OPTION);
    }

    @Override
    public Result saveMenu(Menu menu) {
        int res =  menuMapper.insertSelective(menu);
        if(res > 0){
            return new Result(Result.ErrorCode.SUCCESS_OPTION);
        }else{
            return new Result(Result.ErrorCode.FAIL_OPTION);
        }
    }

    @Override
    public Result deleteMenu(Integer id) {
        int res =  menuMapper.deleteByPrimaryKey(id);
        if(res >0 ){
            return new Result(Result.ErrorCode.SUCCESS_OPTION);
        }else{
            return new Result(Result.ErrorCode.FAIL_OPTION);
        }
    }

    @Override
    public Result updadteMenu(Menu menu) {
        if(Tools.notEmpty(menu) && Tools.notEmpty(menu.getId())){
            int res= menuMapper.updateByPrimaryKeySelective(menu);
            if(res >0){
                return new Result(Result.ErrorCode.SUCCESS_OPTION);
            }else{
                return new Result(Result.ErrorCode.FAIL_OPTION);
            }
        }else{
            return new Result(Result.ErrorCode.BAD_REQUEST);        }
    }

    @Override
    public List<Menu> getMenuByUserId(Integer id) {
        return menuMapper.getByUserId(id);
    }
}
