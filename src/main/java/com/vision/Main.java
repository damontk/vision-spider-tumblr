package com.vision;

import com.vision.core.TumblrStart;
import com.vision.gyfcat.UploadFiled;
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
                "/spring/spring-01redis.xml", "/spring/spring-02http.xml", "/spring/spring-03beans.xml");
        // TumblrStart tumblrStart = (TumblrStart) factory.getBean("tumblrStart");
        // tumblrStart.start();
        UploadFiled uploadFiled = (UploadFiled) factory.getBean("uploadFiled");
        uploadFiled.upload(null);

    }

}
