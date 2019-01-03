package me.nvliu.management.web.entity;

public class RoleMenu {
    private int roleId;
    private int menuId;

    public RoleMenu() {
    }

    public RoleMenu(int roleId, int menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
