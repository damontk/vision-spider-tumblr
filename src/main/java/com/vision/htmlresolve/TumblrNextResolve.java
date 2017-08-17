package com.vision.htmlresolve;

import com.google.common.collect.Sets;
import com.vision.cache.BlogCache;
import com.vision.cache.VideoCache;
import com.vision.constant.TumblrElementConstant;
import com.vision.constant.TumblrEnum;
import com.vision.entity.EntityValue;
import com.vision.entity.TumblrBlogEntity;
import com.vision.entity.TumblrVideoEntity;
import com.vision.mq.RedisMqPut;
import com.vision.util.http.down.thread.DownThread;
import com.vision.util.http.down.thread.DownVideoExecutorPool;
import com.vision.util.http.exception.RequestDeniedException;
import com.vision.util.http.util.HttpRequestDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author zhongc
 * @version 1.0
 * @Filename TumblrNextResolve.java
 * <p>
 * Description
 * @Email zhong_ch@foxmail.com
 * @History <li>Author: zhongc</li>
 * <li>Date: 2017/1/15</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
@Component
public class TumblrNextResolve {

    private static final Logger logger = LoggerFactory.getLogger(TumblrNextResolve.class);

    @Resource
    private BlogCache blogCache;

    @Resource
    private VideoCache videoCache;

    @Resource
    private RedisMqPut redisMqPut;

    @Resource
    private HttpRequestDao tumblrHttpRequestDao;

    @Resource
    private DownVideoExecutorPool<TumblrVideoEntity> executorPool;

    @Value("${needDown}")
    private Boolean needDown;

    /**
     * 获取视频url连接
     * 获取follow 、like的url 并将其放到缓存中
     * 返回 like的视频url
     *
     * @param blogName   博客名称  前缀
     * @param tumblrEnum 类型
     */
    public void getBlogUrlSet(String blogName, TumblrEnum tumblrEnum) throws Exception {
        String url = getBlogUrls(blogName, tumblrEnum);
        Set<String> urlList = Sets.newHashSet();
        //第一次进入的(关注列表第一页)
        logger.info("请求关注列表 url:{}", url);
        String firstHtml = tumblrHttpRequestDao.getWebPage(url);
        Document parse = Jsoup.parse(firstHtml);
        String title = parse.title();
        if (TumblrElementConstant.REQUEST_DENIED.equals(title)) {
            throw new RequestDeniedException("访问该博客关注列表错误未获取到权限,url:" + url);
        }
        urlList.addAll(TumblrHtmlResolve.getTumblrUrls(firstHtml));
        String nextHref = getNextPage(firstHtml);
        boolean hasNext = nextHref != null;
        while (hasNext) {
            String href = TumblrEnum.HOST.getDesc() + nextHref;
            String nextPageHtml = tumblrHttpRequestDao.getWebPage(href);
            logger.info("url:{}", href);
            urlList.addAll(TumblrHtmlResolve.getTumblrUrls(nextPageHtml));
            nextHref = getNextPage(nextPageHtml);
            hasNext = StringUtils.isNotBlank(nextHref);
        }
        this.putCacheAndMq(urlList);
    }

    public Set<String> getLikedVideoSet(String blogName, TumblrEnum tumblrEnum) throws Exception {
        String url = getBlogUrls(blogName, tumblrEnum);
        Set<String> videoUrlList = Sets.newHashSet();
        //第一次进入的(喜欢列表第一页)
        logger.info("请求喜欢列表列表 url:{}", url);
        String firstHtml = tumblrHttpRequestDao.getWebPage(url);
        Document parse = Jsoup.parse(firstHtml);
        String title = parse.title();
        if (TumblrElementConstant.REQUEST_DENIED.equals(title)) {
            throw new RequestDeniedException("访问博客喜欢列表错误未获取到权限,url:" + url);
        }
        videoUrlList.addAll(TumblrHtmlResolve.getVideoUrl(firstHtml));
        // 获取该页面的博客值并将其加入到缓存、列队中
        this.putCacheAndMq(TumblrHtmlResolve.getTumblrUrls(firstHtml));
        String nextHref = getNextPage(firstHtml);
        boolean hasNext = nextHref != null;
        while (hasNext) {
            String reaUrl = TumblrEnum.HOST.getDesc() + nextHref;
            String nextPageHtml = tumblrHttpRequestDao.getWebPage(reaUrl);
            logger.info("url:{}", reaUrl);
            videoUrlList.addAll(TumblrHtmlResolve.getVideoUrl(nextPageHtml));
            nextHref = getNextPage(nextPageHtml);
            hasNext = StringUtils.isNotBlank(nextHref);
            // 获取该页面的博客值并将其加入到缓存、列队中
            this.putCacheAndMq(TumblrHtmlResolve.getTumblrUrls(nextPageHtml));
        }
        return videoUrlList;
    }


    public void addVideoCache(TumblrBlogEntity blogEntity, String downPath, Set<String> videoUrList, TumblrEnum tumblrEnum) {

        String blogName = blogEntity.getBlogName();
        String reDownPath;
        if (tumblrEnum == TumblrEnum.LIKE) {
            reDownPath = blogName == null ? downPath + "//like" : downPath + blogName + "//like";
        } else {
            reDownPath = downPath + blogName;
        }
        for (String videoUrl : videoUrList) {
            TumblrVideoEntity videoEntity = new TumblrVideoEntity();
            videoEntity.setDownBlogUrl(blogEntity.getUrl());
            videoEntity.setUrl(videoUrl);
            videoEntity.setBlogName(blogName);
            videoEntity.setFileName(videoUrl.substring(videoUrl.lastIndexOf("/tumblr_") + 1, videoUrl.length()));
            if (videoCache.put(videoUrl, videoEntity) && needDown) {
                if (needDown) {
                    //获取到视频地址后  往线程添加下载任务
                    while (executorPool.getThreadCount() >= executorPool.getNThread()) {
                        logger.info("线程满载 等待中.. 线程数:{}", executorPool.getNThread());
                        sleepSomeTime(1000 * 20);
                    }
                    logger.info("获取到视频地址 向下载线程添加下载任务 url:{}", videoUrl);
                    executorPool.putDownVideoTask(new DownThread<>(videoEntity, reDownPath), false);
                }
            } else {
                logger.warn("缓存中已经存在该视频  过滤下载.....");
            }
        }
    }

    private void sleepSomeTime(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            logger.error("睡眠失败:{}", e);
        }
    }


    private String getBlogUrls(String blogName, TumblrEnum tumblrEnum) {
        String url;
        switch (tumblrEnum) {
            // 登陆账户
            case LOGIN_FOlLOW:
            case LOGIN_LIKE:
                url = tumblrEnum.getDesc();
                break;
            // 非登陆博客
            case FOlLOW:
            case LIKE:
                Assert.notNull(blogName, "博客名称为空！");
                url = String.format(tumblrEnum.getDesc(), blogName);
                break;
            default:
                throw new IllegalArgumentException("获取博客地址错误，不合法的参数:{}" + tumblrEnum.getDesc());
        }
        return url;

    }


    /**
     * 将 url放入缓存和列队中
     * 如果存在 缓存列队均不添加
     *
     * @param videoUrls blog Url
     */
    public void putCacheAndMq(Set<String> videoUrls) {
        if (CollectionUtils.isNotEmpty(videoUrls)) {
            for (String url : videoUrls) {
                TumblrBlogEntity tumblrBlogEntity = EntityValue.setBlogEntityValue(url);
                if (blogCache.put(url, tumblrBlogEntity)) {
                    redisMqPut.putMq(url);
                }
            }
        }
    }

    /**
     * 获取下一页
     *
     * @param html
     * @return
     */
    private static String getNextPage(String html) {
        String nextHref = null;
        Document document = Jsoup.parse(html);
        Element pageElement = document.getElementById(TumblrElementConstant.DIV_ID_PAGINATION);
        if (pageElement != null) {
            Elements elementsByTag = pageElement.getElementsByTag(TumblrElementConstant.TAG_A);
            for (Element element : elementsByTag) {
                if (element.hasClass("next")) {
                    nextHref = elementsByTag.last().attr(TumblrElementConstant.ATTR_HREF);
                }
            }
        }
        return nextHref;
    }


}
