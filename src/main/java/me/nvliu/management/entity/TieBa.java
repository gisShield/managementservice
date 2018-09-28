package me.nvliu.management.entity;

import me.nvliu.management.constants.UtilConstants;
import me.nvliu.management.utils.DateUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.entity
 * @Description:
 * @Date: Create in 16:13 2018/9/17
 * @Modified By:
 */
@Document(collection = "tieba")
public class TieBa  {
    @Id
    private String id;
    /**
     * 帖子主键
     */
    private String postId;
    /**
     * 发帖人名称
     */
    private String userName;
    /**
     * 发帖人ID
     */
    private String userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 帖子描述
     */
    private String describe;
    /**
     * 配图列表
     */
    private List<String> imglist;
    /**
     * 视频列表
     */
    private List<Video> videoList;
    /**
     * 收集时间
     */
    private Date time;
    /**
     * 标签
     */
    private List<String> tags;

    public TieBa() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public List<String> getImglist() {
        return imglist;
    }

    public void setImglist(List<String> imglist) {
        this.imglist = imglist;
    }

    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }

    public String getTime() {
        String t = null;
        if(this.time != null){
            t = DateUtil.formatDate(this.time, UtilConstants.FORMATDATE_Y_M_D_T);
        }
        return t;
    }
    public void setTime(Date time) {
        this.time = time;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "TieBa{" +
                "id='" + id + '\'' +
                ", postId='" + postId + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", describe='" + describe + '\'' +
                ", imglist=" + imglist +
                ", videoList=" + videoList +
                ", time=" + getTime()    +
                ", tags=" + tags +
                '}';
    }
}
