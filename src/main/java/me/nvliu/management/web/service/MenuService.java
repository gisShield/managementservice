package me.nvliu.management.web.service;

import com.github.pagehelper.PageInfo;
import me.nvliu.management.web.entity.Menu;
import me.nvliu.management.web.entity.Result;

import java.util.List;

public interface MenuService {
    /**
     * 通过菜单ID获取菜单信息
     * @param id
     * @return
     */
    Result getMenuById(Integer id);


    /**
     * 获取菜单列表信息
     * @param menu
     * @return
     */
    Result getMenuList(Menu menu);

    /**
     * 获取菜单分页信息
     * @param menu
     * @param pageNumber
     * @param pageSize
     * @return
     */
    Result getMenuPage(String name,Integer parentId, int pageNumber, int pageSize);

    /**
     * 保存菜单信息
     * @param menu
     * @return
     */
    Result saveMenu(Menu menu);

    /**
     * 删除菜单信息
     * @param id
     * @return
     */
    Result deleteMenu(Integer id);

    /**
     * 更新菜单信息
     * @param menu
     * @return
     */
    Result updadteMenu(Menu menu);

    /**
     * 通过用户Id获取菜单列表
     * @param id
     * @return
     */
    List<Menu> getMenuByUserId(Integer id);

}
