package me.nvliu.management.web.security;

import me.nvliu.management.web.entity.Menu;

import java.io.Serializable;
import java.util.List;

public class JwtAuthenticationResponse implements Serializable {
    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;
    private  String uuid;
    private  List<Object> menus;
    private String name;

    public JwtAuthenticationResponse(String token, String uuid, List<Object> menus, String name) {
        this.token = token;
        this.uuid = uuid;
        this.menus = menus;
        this.name = name;
    }

    public JwtAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Object> getMenus() {
        return menus;
    }

    public void setMenus(List<Object> menus) {
        this.menus = menus;
    }

    public JwtAuthenticationResponse(String token, String uuid, List<Object> menus) {
        this.token = token;
        this.uuid = uuid;
        this.menus = menus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}