package me.nvliu.management.web.service;

import com.github.pagehelper.PageInfo;
import me.nvliu.management.web.entity.Menu;

import java.util.List;

public interface MenuService {
    /**
     * 通过菜单ID获取菜单信息
     * @param id
     * @return
     */
    Menu getMenuById(Integer id);


    /**
     * 获取菜单列表信息
     * @param menu
     * @return
     */
    List<Menu> getMenuList(Menu menu);

    /**
     * 获取菜单分页信息
     * @param menu
     * @param pageNumber
     * @param pageSize
     * @return
     */
    PageInfo<Menu> getMenuPage(Menu menu, int pageNumber, int pageSize);

    /**
     * 保存菜单信息
     * @param menu
     * @return
     */
    int saveMenu(Menu menu);

    /**
     * 删除菜单信息
     * @param id
     * @return
     */
    int deleteMenu(Integer id);

    /**
     * 更新菜单信息
     * @param menu
     * @return
     */
    int updadteMenu(Menu menu);

}
