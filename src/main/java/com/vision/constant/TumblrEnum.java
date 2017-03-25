package com.vision.constant;

/**
 * 项目名称：com.zhongc
 * 类名称： TumblrEnum
 * 类描述：
 * 创建人：zc
 * 创建时间：2017-01-01 19:04
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public enum TumblrEnum {
    /**
     * 主页
     */
    HOST(1, TumblrUrlConstant.TUMBLR_HOST),

    /**
     * 喜欢
     */
    LOGIN_LIKE(2, TumblrUrlConstant.TUMBLR_LIKE),


    /**
     * 关注
     */
    LOGIN_FOlLOW(3, TumblrUrlConstant.TUMBLR_FOLLOWING),

    /**
     * 博主
     */
    BLOG_HOST(4, ""),

    /**
     * 喜欢
     */
    LIKE(5, TumblrUrlConstant.BLOGGER_LIKED),

    /**
     * 关注
     */
    FOlLOW(6, TumblrUrlConstant.BLOGGER_FOLLOWING);

    private int code;
    private String desc;

    TumblrEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

}
