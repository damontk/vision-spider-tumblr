package com.vision.entity;

/**
 * 项目名称：vision
 * 类名称： EntityValue
 * 类描述：
 * 创建人：zc
 * 创建时间：2017-01-18 15:28
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class EntityValue {

    /**
     * 设置博客缓存
     *
     * @param followsUrl 博客地址
     * @param totalPage  总页数
     * @return
     */
    public static TumblrBlogEntity setBlogEntityValue(String followsUrl, int totalPage) {
        TumblrBlogEntity blogEntity = new TumblrBlogEntity();
        String blogName = followsUrl.substring(followsUrl.indexOf("://") + 3, followsUrl.lastIndexOf(".tumblr"));
        blogEntity.setBlogName(blogName);
        blogEntity.setUrl(followsUrl);
        blogEntity.setTotalPage(totalPage);
        return blogEntity;
    }


    /**
     * 设置博客缓存
     *
     * @param followsUrl 博客地址
     * @return
     */
    public static TumblrBlogEntity setBlogEntityValue(String followsUrl) {
        TumblrBlogEntity blogEntity = new TumblrBlogEntity();
        String blogName = followsUrl.substring(followsUrl.indexOf("://") + 3, followsUrl.lastIndexOf(".tumblr"));
        blogEntity.setBlogName(blogName);
        blogEntity.setUrl(followsUrl);
        return blogEntity;
    }
}
