package com.vision.constant;

/**
 * 项目名称：vision
 * 类名称： RegExpConstant
 * 类描述：正则表达式
 * 创建人：zc
 * 创建时间：2017-01-03 17:46
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class RegExpConstant {

    /**
     * 数字
     */
    public static final  String NUM_REG_EX = "[^0-9]";

    /**
     * tumblr 地址匹配
     */
    public static final  String TUMBLR_REGEX = "[htps]+://[A-Za-z0-9]*((\\w+)(\\.tumblr.com/))";
    public static final  String TUMBLR_REGEX1 = "http://.*.gan003.com.*.mp4/";
}
