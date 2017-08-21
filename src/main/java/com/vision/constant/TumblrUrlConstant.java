package com.vision.constant;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：vision
 * 类名称： TumblrUrlConstant
 * 类描述： tumblr 静态常量
 * 创建人：zc
 * 创建时间：2016-12-27 17:37
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class TumblrUrlConstant {

    /**
     * tumblr HOST
     */
    public static final String TUMBLR_HOST = "https://www.tumblr.com/";

    /**
     * tumblr登录后主页
     */
    public static final String TUMBLR_DASHBOARD = "https://www.tumblr.com/dashboard";

    /**
     * 关注列表
     */
    public static final String TUMBLR_FOLLOWING = "https://www.tumblr.com/following";
    /**
     * LIKE列表
     */
    public static final String TUMBLR_LIKE = "https://www.tumblr.com/likes";

    /**
     * 博主关注地址
     */
    public static final String BLOGGER_FOLLOWING = "https://www.tumblr.com/followed/by/%s";

    /**
     * 博主喜欢地址
     */
    public static final String BLOGGER_LIKED = "https://www.tumblr.com/liked/by/%s";

    /**
     * 登录页面地址
     */
    public static final String TUMBLR_LOGIN_URL = "https://www.tumblr.com/login";

    public static final String TUMBLR_ARCHIVE = "/archive";

    /**
     * 验证用户名请求地址
     */
    public static final String VALIDATE_ACCOUNT_URL = "https://www.tumblr.com/svc/account/register";

    /**
     * tumblr 原生视频真实的url
     */
    public static final String VIDEO_REAL_URL = "https://vtt.tumblr.com/";

    /**
     * 直接找到所有的视频？
     */
    public static final String a = "tagged/video";

    public static final List<String> NOT_MATCH_URL_LIST = Lists.newArrayList(
            "//68.media.", "//static.", "//assets.", "//px.srvcs.", "www.tumblr.com",
            "putin1234", "xiaoxiaobai12138", "mygirlfrienddaily", "lingzai01", "jackwahahaha", "honey886", "ecupniuniu");

    public static void main(String[] args) {
        String html = "匹配：http://www.gan003.com/styles/all-responsive-metal.css?v=3.1\" rel=\"stylesheet\" type=\"text/css\"/>\t";
        Pattern pattern = Pattern.compile(RegExpConstant.TUMBLR_REGEX1);
        Matcher m = pattern.matcher(html);
        while (m.find()) {
            System.out.println(m.group());
        }
    }


}
