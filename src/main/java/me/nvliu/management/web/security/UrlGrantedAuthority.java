package me.nvliu.management.web.security;

import org.springframework.security.core.GrantedAuthority;

public class UrlGrantedAuthority implements GrantedAuthority {
    /**
     * 请求路径
     */
    private String method;

    public UrlGrantedAuthority(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getAuthority() {
        return this.method;
    }
}
