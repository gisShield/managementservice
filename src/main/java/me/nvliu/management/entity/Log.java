package me.nvliu.management.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.auditing
 * @Description: 日志记录类
 * @Date: Create in 16:01 2018/9/27
 * @Modified By:
 */
@Document(collection = "log")
public class Log {
    @Id
    private String id;
    private String relationId;
    private String auditing;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getAuditing() {
        return auditing;
    }

    public void setAuditing(String auditing) {
        this.auditing = auditing;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
