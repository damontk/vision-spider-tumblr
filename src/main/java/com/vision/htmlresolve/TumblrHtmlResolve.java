package com.vision.htmlresolve;

import com.google.common.collect.Sets;
import com.vision.constant.RegExpConstant;
import com.vision.constant.TumblrElementConstant;
import com.vision.constant.TumblrUrlConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目名称：vision
 * 类名称： TumblrHtmlResolve
 * 类描述：
 * 创建人：zc
 * 创建时间：2016-12-29 10:13
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class TumblrHtmlResolve {

    private static final  Logger logger = LoggerFactory.getLogger(TumblrHtmlResolve.class);

    /**
     * 使用正则表达式 匹配出所有的博客地址
     *
     * @param html 当前页面
     * @return
     */
    public static Set<String> getTumblrUrls(String html) {
        Pattern pattern = Pattern.compile(RegExpConstant.TUMBLR_REGEX);
        Matcher m = pattern.matcher(html);
        Set<String> urls = Sets.newHashSet();
        while (m.find()) {
            urls.add(m.group());
        }
        Set<String> relUrls = Sets.newHashSet();
        // 执行过滤
        for (String url : urls) {
            boolean contain = false;
            for (String filter : TumblrUrlConstant.NOT_MATCH_URL_LIST) {
                if (url.contains(filter)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                relUrls.add(url);
            }
        }
        return relUrls;
    }
    /**
     * 获取关注用户的数量
     *
     * @param html 进入关注列表html
     * @return 关注用户数量
     */
    public static int getFollowNum(String html) {
        int num = 0;
        Document parse = Jsoup.parse(html);
        Element element = parse.getElementById(TumblrElementConstant.DIV_ID_FOLLOWS);
        if (element != null && element.children() != null) {
            Element followText = element.children().get(1);
            // 在 Tumblr 上关注 **
            String text = followText.text();
            // 获取 ** 数字 的值
            Pattern p = Pattern.compile(RegExpConstant.NUM_REG_EX);
            Matcher m = p.matcher(text);
            num = Integer.valueOf(m.replaceAll("").trim());
        }
        return num;
    }

    /**
     * 关注列表下一页  tumblr以每次25页为默认size
     *
     * @param html
     * @return
     */
    public static String getFollowNext(String html) {
        Document parse = Jsoup.parse(html);
        parse.getElementById(TumblrElementConstant.DIV_ID_FOLLOWS_BLOG);
        return "";
    }

    /**
     * 获取视频地址
     * <p>视频分为 原生（存储在tumblr的）其它应用的</p>
     * 1. 原生的 原始url为
     * <p>e.g:</p>
     * <p>1)< video id="embed-586b3f14e96fc360319615_html5_api" class="vjs-tech" poster="https://68.media.tumblr.com/tumblr_oj6fobWwPq1txnqlp_smart1.jpg" preload="none" muted="" data-crt-video="">
     * < source src="https://www.tumblr.com/video_file/t:Thf1PylvW-ZqaVngF2Y3QQ/155331301180/tumblr_oj6fobWwPq1txnqlp" type="video/mp4">
     * </ video></p>
     * 2. 其它转载
     * e.g:
     * <p>1) < video class="_c8hkj" poster="https://scontent.cdninstagram.com/t51.2885-15/s640x640/e15/15876413_298652120536547_8763701650171887616_n.jpg?ig_cache_key=MTQxODYxOTEyNTQ0MTQ5MzM5NQ%3D%3D.2"
     * preload="none" src="https://scontent.cdninstagram.com/t50.2886-16/15872290_1327894227282407_9109118648286445568_n.mp4" type="video/mp4">
     * </ video></p>
     * 2) < video tabindex="-1" class="video-stream html5-main-video" style="width: 500px; height: 281px; left: 0px; top: 0px;" src="blob:https://www.youtube.com/a4c71612-b5ca-4739-85a6-0b7c75990989">< /video>
     *
     * @param html
     * @return
     */
    public static Set<String> getVideoUrl(String html) {
        Set<String> videoUrlList = Sets.newHashSet();
        // get video tag
        Document document = Jsoup.parse(html);
        Elements videoTags = document.getElementsByTag(TumblrElementConstant.TAG_VIDEO);
        if (CollectionUtils.isNotEmpty(videoTags)) {
            //循环
            for (Element videoTag : videoTags) {
                String videoUrl;
                Elements sourceTags = videoTag.getElementsByTag(TumblrElementConstant.TAG_SOURCE);
                //如果 source 不为空 则取 source中的值
                if (CollectionUtils.isNotEmpty(sourceTags)) {
                    Element sourceTag = sourceTags.get(0);
                    videoUrl = sourceTag.attr(TumblrElementConstant.ATTR_SRC);
                    String type = sourceTag.attr(TumblrElementConstant.ATTR_TYPE);
                    videoUrl += StringUtils.isNotBlank(type) ? "." + type.substring(type.indexOf("/") + 1, +type.length()) : "";
                } else {
                    // 取 video中的
                    videoUrl = videoTag.attr(TumblrElementConstant.ATTR_SRC);
                }
                //将取到的url转换
                videoUrlList.add(changeVideoUrl(videoUrl));
            }
        }
        return videoUrlList;
    }

    public static int getTotalPage(Document document) {
        int totalPage = 0;
        Element pagination = document.getElementById(TumblrElementConstant.DIV_ID_PAGINATION);
        if (pagination != null) {
            Elements elementsByTag = pagination.getElementsByTag(TumblrElementConstant.TAG_A);
            try {
                totalPage = Integer.valueOf(elementsByTag.attr(TumblrElementConstant.DATA_TOTAL_PAGES));
            } catch (Exception e) {
                logger.info("获取总页数失败：{}", e);
            }
        }
        return totalPage;
    }

    /**
     * @param document 转换后的 document
     * @return
     */
    public static Set<String> getIFrameUrlList(Document document) {
        Set<String> iFrameSet = Sets.newHashSet();
        Elements tVideoContainers = document.getElementsByClass(TumblrElementConstant.CLASS_TUMBLR_VIDEO_CONTAINER);
        for (Element videoContainer : tVideoContainers) {
            Elements iframe = videoContainer.getElementsByTag(TumblrElementConstant.TAG_IFRAME);
            if (CollectionUtils.isNotEmpty(iframe)) {
                iFrameSet.add(iframe.get(0).attr(TumblrElementConstant.ATTR_SRC));
            }
        }
        return iFrameSet;
    }

    private static String changeVideoUrl(String url) {
        String substring = url.substring(url.indexOf("/tumblr_") + 1, url.length());
        String replace = substring.replace("/", "_");
        return TumblrUrlConstant.VIDEO_REAL_URL + replace;
    }


    public static void main(String[] args) throws Exception {
        // String html = "";
        String resource = TumblrHtmlResolve.class.getResource("/").getPath();
        File file = new File(resource + "tumblr_iframe.html");
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader bf = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder();
        String s = "";
        // 读入多个字节到字节数组中，byteread为一次读入的字节数
        while ((s = bf.readLine()) != null) {
            System.out.println(s);
            sb.append(s);
        }
        // String videoUrl = getVideoUrl(sb.toString());


    }
}
