package com.vision.core;

import com.vision.constant.TumblrEnum;
import com.vision.constant.TumblrUrlConstant;
import com.vision.entity.TumblrBlogEntity;
import com.vision.htmlresolve.TumblrNextResolve;
import com.vision.mq.RedisMqGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Set;

/**
 * 项目名称：vision
 * 类名称： TumblrStart
 * 类描述：
 * 创建人：zc
 * 创建时间：2017-01-06 15:21
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class TumblrStart extends Thread {

    private final Logger logger = LoggerFactory.getLogger(TumblrStart.class);

    private String cookiePath;

    private String userName;

    private String pwd;

    private String downPath;

    @Resource
    private TumblrNextResolve tumblrNextResolve;

    @Resource
    private RedisMqGet redisMqGet;

    @Resource
    private BlogStart blogStart;

    @Resource
    private TumblrLoginHandler tumblrLoginHandler;


    @Override
    public void run() {
        try {
            this.begin();
        } catch (Exception e) {
            logger.info("启动异常:{}", e);
        }
    }

    private void begin() throws Exception {
        tumblrLoginHandler.TumblrLogin(userName, pwd, cookiePath);
        // 登录成功 获取该账户的关注列表
        try {
             logger.info("登陆成功 获取关注列表...");
             tumblrNextResolve.getBlogUrlSet("userName", TumblrEnum.LOGIN_FOlLOW);
             // logger.info("获取关注列表结束");
             logger.info("获取喜欢列表...");
             Set<String> likedVideoSet = tumblrNextResolve.getLikedVideoSet(null, TumblrEnum.LOGIN_LIKE);
             TumblrBlogEntity entity = new TumblrBlogEntity();
             entity.setBlogName("loginUser");
             entity.setUrl(TumblrUrlConstant.TUMBLR_HOST);
             tumblrNextResolve.dealVideo(entity, downPath, likedVideoSet, TumblrEnum.HOST);
             logger.info("获取喜欢列表结束....");
//             本博客爬完 开始爬取其它博客列表
            int count = 0;
            while (true) {

                 String oneBlogUrl = redisMqGet.getOneBlogUrl();
                // String oneBlogUrl = "https://ziwei-ziwei.tumblr.com/";
                // String oneBlogUrl = "https://fuckthisallday.tumblr.com/";
                // String oneBlogUrl = "http://hotcumisyum.tumblr.com/";//欧美
                // String oneBlogUrl = "https://yy7777777.tumblr.com";
                // String oneBlogUrl = "https://zerovos.tumblr.com";
                // String oneBlogUrl = "https://qswer123.tumblr.com/";
                // String oneBlogUrl = "https://whhdokemme.tumblr.com/";
                // String oneBlogUrl = "https://dadad.tumblr.com";
                // String oneBlogUrl = "https://vinzzkoo.tumblr.com";
                // String oneBlogUrl = "https://woxiaosaobi.tumblr.com";
                // String oneBlogUrl = "http://zinggoro.tumblr.com";
                // String oneBlogUrl = "http://super-foxxxxy.tumblr.com";
                // String oneBlogUrl = "https://monvshishang.tumblr.com";
                //https://monvshishang.tumblr.com
                // https://peacoxk.tumblr.com
                // https://omilove3325.tumblr.com
                // https://hanguo3.tumblr.com/
                // https://sex9988.tumblr.com
                // https://jason5581.tumblr.com/
                // https://jaccy015.tumblr.com
                // https://instagram26.tumblr.com/
                // https://sorkrhek66.tumblr.com
                // https://duck-kim.tumblr.com/
                // https://2013117.tumblr.com
                // https://choihyeona.tumblr.com
                // https://1-23456789-0.tumblr.com\
                // https://91nude.tumblr.com/
                // https://youknows8.tumblr.com
                //https://mjj1024.tumblr.com
                // https://wangtz.tumblr.com
                // https://innerpersonamiracle.tumblr.com
                // https://meiyaochacha.tumblr.com
                // https://1286261230.tumblr.com
                // https://yy7777777.tumblr.com


                // String oneBlogUrl = redisMqGet.getOneBlogUrl();
//                String oneBlogUrl = "https://mingzzi-bozi.tumblr.com";
                if (oneBlogUrl == null) {
                    logger.warn("不存在下载博客地址....休眠1分钟");
                    count++;
                    try {
                        Thread.sleep(count * 60);
                    } catch (InterruptedException e) {
                        logger.error("睡眠失败:{}", e);
                    }
                } else {
                    logger.info("获取到博客地址 开始请求... url:{}", oneBlogUrl);
                    blogStart.archiveStart("https://tumb-korea-sexy44.tumblr.com", downPath);
                }
                // break;
            }
        } catch (Exception e) {
            logger.error("异常", e);
        }
        logger.info("爬虫结束........");
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setDownPath(String downPath) {
        this.downPath = downPath;
    }

    // public void setTumblrNextResolve(TumblrNextResolve tumblrNextResolve) {
    //     this.tumblrNextResolve = tumblrNextResolve;
    // }
    //
    // public void setRedisMq(RedisMq redisMqGet) {
    //     this.redisMqGet = redisMqGet;
    // }
    //
    // public void setBlogStart(BlogStart blogStart) {
    //     this.blogStart = blogStart;
    // }
}
