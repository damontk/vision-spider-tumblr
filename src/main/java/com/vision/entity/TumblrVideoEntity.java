package com.vision.entity;

import com.vision.constant.DownStatusEnum;

/**
 * 项目名称：vision
 * 类名称： TumblrVideoEntity
 * 类描述：
 * 创建人：zc
 * 创建时间：2016-12-28 9:59
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class TumblrVideoEntity {

    /**
     * 视频url
     */
    private String url;

    /**
     * iframe地址
     */
    private String iframeUrl;

    /**
     * 视频下载的博客
     */
    private String downBlogUrl;

    /**
     * 源博客地址
     */
    private String sourceBlogUrl;

    /**
     * 是否下载完成
     */
    private DownStatusEnum statusEnum;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIframeUrl() {
        return iframeUrl;
    }

    public void setIframeUrl(String iframeUrl) {
        this.iframeUrl = iframeUrl;
    }

    public String getDownBlogUrl() {
        return downBlogUrl;
    }

    public void setDownBlogUrl(String downBlogUrl) {
        this.downBlogUrl = downBlogUrl;
    }

    public String getSourceBlogUrl() {
        return sourceBlogUrl;
    }

    public void setSourceBlogUrl(String sourceBlogUrl) {
        this.sourceBlogUrl = sourceBlogUrl;
    }

    public DownStatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(DownStatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }
}
