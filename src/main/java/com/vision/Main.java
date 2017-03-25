package com.vision;

import com.vision.core.TumblrStart;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 项目名称：vision
 * 类名称： Main
 * 类描述：
 * 创建人：zc
 * 创建时间：2016-12-28 14:05
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class Main {
    // 爬取页面线程 使用单线程执行

    public static void main(String[] args) throws Exception {
        System.setProperty("sun.net.client.defaultConnectTimeout", String
                .valueOf(10000));// （单位：毫秒）
        System.setProperty("sun.net.client.defaultReadTimeout", String
                .valueOf(10000)); // （单位：毫秒
        System.setProperty("https.protocols", "TLSv1.1");
        ApplicationContext factory = new ClassPathXmlApplicationContext(
                "/spring/spring-01redis.xml", "/spring/spring-02beans.xml", "/spring/spring-03http.xml");
        TumblrStart tumblrStart = (TumblrStart) factory.getBean("tumblrStart");
        // TumblrNextResolve tumblrNexResolve = (TumblrNextResolve) factory.getBean("tumblrNextResolve");
        // tumblrNexResolve.putCacheAndMq(Sets.newHashSet("http://8989892.tumblr.com/","http://82989892.tumblr.com/"));
        tumblrStart.start();

        // System.out.println(MediaType.parse("video/mp4").is(MediaType.ANY_VIDEO_TYPE));


        // Map<String,String> map = new HashMap<>();
        // map = null;
        // map.isEmpty();

    }

}
