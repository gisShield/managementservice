package me.nvliu.management.web.vo;

import java.io.Serializable;

/**
 * @author mvp
 */
public class UserVo implements Serializable{
    private Integer id;
    private String name;
    private int unusable;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnusable() {
        return unusable;
    }

    public void setUnusable(int unusable) {
        this.unusable = unusable;
    }
}
