package me.nvliu.management.entity;


import me.nvliu.management.constants.UtilConstants;
import me.nvliu.management.utils.DateUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author:mvp
 * @ProjectName: restservice
 * @PacketName: com.nvliu.restservice.entity
 * @Description: 定时任务配置类
 * @Date: Create in 14:52 2018/6/26
 * @Modified By:
 */
@Document(collection = "task")
public class TaskConf implements Serializable{
    @Id
    private int id;
    private String taskName;
    private String taskDescription;
    private String taskClass;
    private String cron;
    private int status; // true 启动  false 关闭
    private Date lastTimeExe;
    private String url;

    public TaskConf() {
    }

    public TaskConf(int id, String taskName, String taskDescription, String taskClass, String cron, int status, Date lastTimeExe, String url) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskClass = taskClass;
        this.cron = cron;
        this.status = status;
        this.lastTimeExe = lastTimeExe;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(String taskClass) {
        this.taskClass = taskClass;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLastTimeExe() {
        if(this.lastTimeExe != null){
            return DateUtil.formatDate(this.lastTimeExe, UtilConstants.FORMATDATE_Y_M_D_T);
        }
        return  null;
    }

    public void setLastTimeExe(Date lastTimeExe) {
        this.lastTimeExe = lastTimeExe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TaskConf{" +
                "id=" + id +
                ", taskName='" + taskName + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskClass='" + taskClass + '\'' +
                ", cron='" + cron + '\'' +
                ", status=" + status +
                ", lastTimeExe=" + getLastTimeExe() +
                ", url='" + url + '\'' +
                '}';
    }
}
