package me.nvliu.management.entity;

/**
 * @Author:mvp
 * @ProjectName: management
 * @PacketName: me.nvliu.management.entity
 * @Description:
 * @Date: Create in 15:44 2018/9/17
 * @Modified By:
 */
public class Video {
    private String title;
    private String image;
    private String url;

    public Video() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
