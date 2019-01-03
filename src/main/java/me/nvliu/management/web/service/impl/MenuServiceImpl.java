package me.nvliu.management.web.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.nvliu.management.utils.Tools;
import me.nvliu.management.web.dao.MenuMapper;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Menu getMenuById(Integer id) {
        Menu menu = null;
        if(Tools.notEmpty(id)){
            menu = menuMapper.selectByPrimaryKey(id);
        }
        return menu;
    }

    @Override
    public List<Menu> getMenuList(Menu menu) {

        return menuMapper.getMenuList(menu);
    }

    @Override
    public PageInfo<Menu> getMenuPage(Menu menu, int pageNumber, int pageSize) {
        PageInfo<Menu> menuPageInfo = null ;
        int p = 1;
        int s = 10;
        if(Tools.notEmpty(pageNumber)){
            p = pageNumber;

        }
        if(Tools.notEmpty(pageSize)){
            s = pageSize;
        }
        if(Tools.notEmpty(menu)){
            PageHelper.startPage(p,s);
            menuPageInfo = new PageInfo<>(menuMapper.getMenuList(menu));
        }
        return menuPageInfo;
    }

    @Override
    public int saveMenu(Menu menu) {
        return menuMapper.insertSelective(menu);
    }

    @Override
    public int deleteMenu(Integer id) {
        return menuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updadteMenu(Menu menu) {
        if(Tools.notEmpty(menu) && Tools.notEmpty(menu.getId())){
            return menuMapper.updateByPrimaryKeySelective(menu);
        }else{
            return -1;
        }
    }
}
