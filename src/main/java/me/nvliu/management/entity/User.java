package me.nvliu.management.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.entity
 * @Description:
 * @Date: Create in 11:17 2018/9/27
 * @Modified By:
 */
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String appId;
    private String clientId;
    private String alias;
    private String type;
    private String cookie;
    private Boolean isRing;
    private Boolean isVibrate;
    private Boolean isClearable;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public Boolean getRing() {
        return isRing;
    }

    public void setRing(Boolean ring) {
        isRing = ring;
    }

    public Boolean getVibrate() {
        return isVibrate;
    }

    public void setVibrate(Boolean vibrate) {
        isVibrate = vibrate;
    }

    public Boolean getClearable() {
        return isClearable;
    }

    public void setClearable(Boolean clearable) {
        isClearable = clearable;
    }
}
