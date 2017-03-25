package com.vision.entity;

/**
 * 项目名称：com.zhongc
 * 类名称： TumblrBlogEntity
 * 类描述：
 * 创建人：zc
 * 创建时间：2016-12-27 23:42
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class TumblrBlogEntity {

    /**
     * 博客名称
     */
    private String blogName;

    /**
     * blog Url
     */
    private String url;

    /**
     * 下载页数
     */
    private int downPage = 1;

    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 是否下载完成
     */
    private int isDown = 0;

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDownPage() {
        return downPage;
    }

    public void setDownPage(int downPage) {
        this.downPage = downPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getIsDown() {
        return isDown;
    }

    public void setIsDown(int isDown) {
        this.isDown = isDown;
    }
}
