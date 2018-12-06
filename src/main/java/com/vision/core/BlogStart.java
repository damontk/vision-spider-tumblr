package com.vision.core;

import com.vision.cache.BlogCache;
import com.vision.constant.TumblrElementConstant;
import com.vision.constant.TumblrEnum;
import com.vision.constant.TumblrUrlConstant;
import com.vision.entity.EntityValue;
import com.vision.entity.TumblrBlogEntity;
import com.vision.htmlresolve.TumblrHtmlResolve;
import com.vision.htmlresolve.TumblrNextResolve;
import com.vision.util.http.exception.RequestDeniedException;
import com.vision.util.http.exception.RequestFailedException;
import com.vision.util.http.util.HttpRequestDao;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Filename BlogStart.java
 * Description 开始爬取一个博客
 * 1. 循环获取当前博客
 * <p>
 * 2. 获取当前博客的like
 * <p>
 * 3. 获取当前博客的follow
 *
 * @author zhongc
 *         email: uusipders@gmail.com
 *         History <li>Author: zhongc</li>
 *         <li>Date: 2017/1/15</li>
 *         <li>Version: 1.0</li>
 *         <li>Content: create</li>
 * @version 1.0
 */
@Component
public class BlogStart {

    private final Logger logger = LoggerFactory.getLogger(BlogStart.class);


    /**
     * blog cache
     */
    @Resource
    private BlogCache blogCache;

    @Resource
    private TumblrNextResolve tumblrNextResolve;

    @Resource
    private HttpRequestDao tumblrHttpRequestDao;

    private String downPath;

    /**
     * 通过时间节点直接获取博客信息
     */
    public void archiveStart(String url, String downPath) {
        this.downPath = downPath;
        try {
            if (url.endsWith("/")) {
                url = url.substring(0, url.length() - 1);
            }
            String realUrl;
            realUrl = url + TumblrUrlConstant.TUMBLR_ARCHIVE;
            Element nextPageURL = archiveNextEach(url, realUrl);
            while (nextPageURL != null) {
                String nextPage = url + nextPageURL.attr(TumblrElementConstant.ATTR_HREF);
                this.archiveNextEach(url, nextPage);
            }

        } catch (RequestFailedException e) {
            e.printStackTrace();
        }

    }

    /**
     * archive 下一页循环
     *
     * @param realUrl
     * @return
     * @throws RequestFailedException
     */
    private Element archiveNextEach(String url, String realUrl) throws RequestFailedException {
        String firstPage = tumblrHttpRequestDao.getWebPage(realUrl);
        Document parse = Jsoup.parse(firstPage);
        Elements videoDivs = parse.getElementsByClass(TumblrElementConstant.ARCHIVE_DIV_CLASS_IS_VIDEO);
        Element nextPageURL = parse.getElementById(TumblrElementConstant.DIV_ID_NEXT_PAGE);
        for (Element videoDiv : videoDivs) {
            Elements tagAs = videoDiv.getElementsByTag(TumblrElementConstant.TAG_A);
            if (CollectionUtils.isNotEmpty(tagAs)) {
                String videoUrl = tagAs.get(0).attr(TumblrElementConstant.ATTR_HREF);
                String realHtml = tumblrHttpRequestDao.getWebPage(videoUrl);
                TumblrBlogEntity blogEntity = EntityValue.setBlogEntityValue(url, -1);
                eachFrame(TumblrHtmlResolve.getIFrameUrlList(Jsoup.parse(realHtml)), blogEntity);
            }
        }
        return nextPageURL;
    }


    public void start(String url, String downPath) {
        this.downPath = downPath;
        try {
            logger.info("请求博客 url:{}", url);
            String firstPage = tumblrHttpRequestDao.getWebPage(url);
            // tumblrNextResolve.putCacheAndMq(TumblrHtmlResolve.getTumblrUrls(firstPage));
            Document fistPageDocument = Jsoup.parse(firstPage);
            int totalPage = TumblrHtmlResolve.getTotalPage(fistPageDocument);
            TumblrBlogEntity blogEntity = EntityValue.setBlogEntityValue(url, totalPage);
            blogCache.put(url, blogEntity);
            // 循环 获取iframe对应的真实的页面取到视频url
            eachFrame(TumblrHtmlResolve.getIFrameUrlList(fistPageDocument), blogEntity);
            // 循环获取博客页面 从第二页开始
            eachBlog(url, totalPage, blogEntity);
            //关注列表
            tumblrNextResolve.getBlogUrlSet(blogEntity.getBlogName(), TumblrEnum.FOlLOW);
            blogLiked(downPath, blogEntity);

        } catch (Exception e) {
            logger.error("获取博客信息异常:{},", e);
        }
    }

    /**
     * 获取喜欢列表 拿到视频信息并加入缓存
     *
     * @param downPath
     * @param blogEntity
     * @throws RequestFailedException
     * @throws RequestDeniedException
     */
    private void blogLiked(String downPath, TumblrBlogEntity blogEntity) throws RequestFailedException, RequestDeniedException {
        //喜欢列表
        Set<String> likedVideoSet = tumblrNextResolve.getLikedVideoSet(blogEntity.getBlogName(), TumblrEnum.LIKE);
        tumblrNextResolve.dealVideo(blogEntity, downPath, likedVideoSet, TumblrEnum.LIKE);
    }

    /**
     * 循环获取博客页面
     *
     * @param followsUrl
     * @param totalPage
     */
    private void eachBlog(String followsUrl, int totalPage, TumblrBlogEntity blogEntity) {
        for (int i = 2; i <= totalPage; i++) {
            String nextPage;
            if (followsUrl.endsWith("/")) {
                nextPage = followsUrl + "page/" + i;
            } else {
                nextPage = followsUrl + "/page/" + i;
            }
            // 请求下一页
            logger.info("请求博客 url:{}", nextPage);
            try {
                String nextWebPage = tumblrHttpRequestDao.getWebPage(nextPage);
                tumblrNextResolve.putCacheAndMq(TumblrHtmlResolve.getTumblrUrls(nextWebPage));
                // 获取 为video 的 iframe url
                Document nextDocument = Jsoup.parse(nextWebPage);
                // 循环 获取iframe对应的真实的页面取到视频url
                eachFrame(TumblrHtmlResolve.getIFrameUrlList(nextDocument), blogEntity);
                // 睡眠1s
                this.sleepSomeTime(1000);
            } catch (Exception e) {
                logger.error("请求博客 url:{} 异常:{}", nextPage, e);
            }

        }
    }

    /**
     * 循环iframe 获取视频地址 并将获取到的视频加入下载线程池中
     *
     * @param nextFrameUrlList iframe列表
     * @param blogEntity       博客信息
     */
    private void eachFrame(Set<String> nextFrameUrlList, TumblrBlogEntity blogEntity) {
        String followsUrl = blogEntity.getUrl();
        for (String nextFrameUrl : nextFrameUrlList) {
            logger.info("请求iframe获取视频url:{}", nextFrameUrl);
            try {
                String framePage = tumblrHttpRequestDao.getWebPage(nextFrameUrl);
                // 视频地址
                Set<String> videoUrList = TumblrHtmlResolve.getVideoUrl(framePage);
                if (CollectionUtils.isNotEmpty(videoUrList)) {
                    tumblrNextResolve.dealVideo(blogEntity, downPath, videoUrList, TumblrEnum.BLOG_HOST);
                } else {
                    logger.warn("未获取到视频地址  博客地址:{},iframe地址:{}", followsUrl, nextFrameUrl);
                }
            } catch (Exception e) {
                logger.error("请求iframe获取视频 异常url:{},异常:{}", nextFrameUrl, e);
            }

        }
    }

    private void sleepSomeTime(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
