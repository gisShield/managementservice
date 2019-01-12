package me.nvliu.management.web.entity;


import me.nvliu.management.utils.Tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuTree {

    public static Map<String, Object> mapArray = new LinkedHashMap<String, Object>();
    public List<Menu> menuCommon;
    public List<Object> list = new ArrayList<Object>();

    public List<Object> menuList(List<Menu> menu) {
        this.menuCommon = menu;
        for (Menu x : menu) {
            Map<String, Object> mapArr = new LinkedHashMap<String, Object>();
            if (x.getParentId() == 0) {
                mapArr.put("id", x.getId());
                mapArr.put("title", x.getName());
                mapArr.put("level", x.getLevels());
                mapArr.put("pid", x.getParentId());
                if(Tools.notEmpty(x.getIcon())){
                    mapArr.put("icon", x.getIcon());
                }
                if(Tools.notEmpty(x.getUrl())){
                    mapArr.put("path", x.getUrl());
                }
                List<?> children = menuChild(x.getId());
                if(!children.isEmpty() && children.size()>0){
                    mapArr.put("children", children);
                }
                list.add(mapArr);
            }
        }
        return list;
    }


    public List<?> menuChild(int id) {
        List<Object> lists = new ArrayList<Object>();
        for (Menu a : menuCommon) {
            Map<String, Object> childArray = new LinkedHashMap<String, Object>();
            if (a.getParentId() == id) {
                childArray.put("id", a.getId());
                childArray.put("title", a.getName());
                childArray.put("level", a.getLevels());
                childArray.put("pid", a.getParentId());
                if(Tools.notEmpty(a.getIcon())){
                    childArray.put("icon", a.getIcon());
                }
                if(Tools.notEmpty(a.getUrl())){
                    childArray.put("path", a.getUrl());
                }
                List<?> children = menuChild(a.getId());
                if(!children.isEmpty() && children.size()>0){
                    childArray.put("children", children);
                }
                lists.add(childArray);
            }
        }
        return lists;

    }
}
