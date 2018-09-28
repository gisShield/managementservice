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
 * @Date: Create in 14:55 2018/9/15
 * @Modified By:
 */
@Document(collection = "blog")
public class Blog {
    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 微博主键
     */
    private String blogId;
    /**
     * 微博发布时间
     */
    private Date blogTime;
    /**
     * 微博文本
     */
    private String blogText;
    /**
     * 微博用户ID
     */
    private String blogUserId;
    /**
     * 微博用户名称
     */
    private String blogUserName;
    /**
     * 微博包含图片列表
     */
    private List<String> blogImages;
    /**
     * 微博包含视频封面列表
     */
    private String blogVideoImages;
    /**
     * 微博包含视频
     */
    private String blogVideo;
    /**
     * 微博标签
     */
    private List<String> blogTags;

    /**
     * 是否被封禁
     */
    private String isBlock;
    /**
     * 是否违规
     */
    private String isIllegal;

    /**
     * 转发的原微博主键
     */
    private String blogSourceId;
    /**
     * 原微博发布时间
     */
    private Date blogSourceTime;
    /**
     * 原微博内容
     */
    private String blogSourceText;
    /**
     * 原微博用户ID
     */
    private String blogSourceUserId;
    /**
     * 原微博用户名
     */
    private String blogSourceUserName;
    /**
     * 原微博图片列表
     */
    private List<String> blogSourceImages;
    /**
     * 原微博视频封面
     */
    private String blogVideoSourceImages;
    /**
     * 原微博视频
     */
    private String blogVideoSource;

    public Blog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String  getBlogTime() {
        String time = null;
        if(this.blogTime != null){
            time = DateUtil.formatDate(this.blogTime, UtilConstants.FORMATDATE_Y_M_D_T);
        }
        return time;
    }

    public void setBlogTime(Date blogTime) {
        this.blogTime = blogTime;
    }

    public String getBlogText() {
        return blogText;
    }

    public void setBlogText(String blogText) {
        this.blogText = blogText;
    }

    public String getBlogUserId() {
        return blogUserId;
    }

    public void setBlogUserId(String blogUserId) {
        this.blogUserId = blogUserId;
    }

    public String getBlogUserName() {
        return blogUserName;
    }

    public void setBlogUserName(String blogUserName) {
        this.blogUserName = blogUserName;
    }

    public List<String> getBlogImages() {
        return blogImages;
    }

    public void setBlogImages(List<String> blogImages) {
        this.blogImages = blogImages;
    }

    public String getBlogVideoImages() {
        return blogVideoImages;
    }

    public void setBlogVideoImages(String blogVideoImages) {
        this.blogVideoImages = blogVideoImages;
    }

    public String getBlogVideo() {
        return blogVideo;
    }

    public void setBlogVideo(String blogVideo) {
        this.blogVideo = blogVideo;
    }

    public List<String> getBlogTags() {
        return blogTags;
    }

    public void setBlogTags(List<String> blogTags) {
        this.blogTags = blogTags;
    }

    public String getBlogSourceId() {
        return blogSourceId;
    }

    public void setBlogSourceId(String blogSourceId) {
        this.blogSourceId = blogSourceId;
    }

    public String getBlogSourceTime() {
        String time = null;
        if(this.blogSourceTime != null){
            time = DateUtil.formatDate(this.blogSourceTime, UtilConstants.FORMATDATE_Y_M_D_T);
        }
        return time;
    }

    public void setBlogSourceTime(Date blogSourceTime) {
        this.blogSourceTime = blogSourceTime;
    }

    public String getBlogSourceText() {
        return blogSourceText;
    }

    public void setBlogSourceText(String blogSourceText) {
        this.blogSourceText = blogSourceText;
    }

    public String getBlogSourceUserId() {
        return blogSourceUserId;
    }

    public void setBlogSourceUserId(String blogSourceUserId) {
        this.blogSourceUserId = blogSourceUserId;
    }

    public String getBlogSourceUserName() {
        return blogSourceUserName;
    }

    public void setBlogSourceUserName(String blogSourceUserName) {
        this.blogSourceUserName = blogSourceUserName;
    }

    public List<String> getBlogSourceImages() {
        return blogSourceImages;
    }

    public void setBlogSourceImages(List<String> blogSourceImages) {
        this.blogSourceImages = blogSourceImages;
    }

    public String getBlogVideoSourceImages() {
        return blogVideoSourceImages;
    }

    public void setBlogVideoSourceImages(String blogVideoSourceImages) {
        this.blogVideoSourceImages = blogVideoSourceImages;
    }

    public String getBlogVideoSource() {
        return blogVideoSource;
    }

    public void setBlogVideoSource(String blogVideoSource) {
        this.blogVideoSource = blogVideoSource;
    }

    public String isBlock() {
        return isBlock;
    }

    public void setBlock(String block) {
        isBlock = block;
    }

    public String isIllegal() {
        return isIllegal;
    }

    public void setIllegal(String illegal) {
        isIllegal = illegal;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id='" + id + '\'' +
                ", blogId='" + blogId + '\'' +
                ", blogTime=" + getBlogTime() +
                ", blogText='" + blogText + '\'' +
                ", blogUserId='" + blogUserId + '\'' +
                ", blogUserName='" + blogUserName + '\'' +
                ", blogImages=" + blogImages +
                ", blogVideoImages='" + blogVideoImages + '\'' +
                ", blogVideo='" + blogVideo + '\'' +
                ", blogTags=" + blogTags +
                ", isBlock=" + isBlock +
                ", isIllegal=" + isIllegal +
                ", blogSourceId='" + blogSourceId + '\'' +
                ", blogSourceTime=" + getBlogSourceTime() +
                ", blogSourceText='" + blogSourceText + '\'' +
                ", blogSourceUserId='" + blogSourceUserId + '\'' +
                ", blogSourceUserName='" + blogSourceUserName + '\'' +
                ", blogSourceImages=" + blogSourceImages +
                ", blogVideoSourceImages='" + blogVideoSourceImages + '\'' +
                ", blogVideoSource='" + blogVideoSource + '\'' +
                '}';
    }
}
