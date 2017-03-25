package com.vision.htmlresolve;// package com.vision.htmlresolve;
//
// import com.vision.constant.TumblrElementConstant;
// import com.vision.exception.RequestDeniedException;
// import com.vision.util.http.HttpClientCenter;
// import org.jsoup.Jsoup;
// import org.jsoup.nodes.Document;
// import org.jsoup.nodes.Element;
// import org.jsoup.select.Elements;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
//
// import java.util.Set;
//
// /**
//  * 项目名称：com.zhongc
//  * 类名称： TumblrLikesResolve
//  * 类描述：
//  * 创建人：zc
//  * 创建时间：2017-01-07 20:08
//  * 修改人：
//  * 修改时间：
//  * 修改备注：
//  *
//  * @version 1.0
//  */
// public class TumblrLikesResolve {
//     private static final  Logger logger = LoggerFactory.getLogger(TumblrLikesResolve.class);
//
//     // public static void getLikeVideo(Set<String> iFrameUrlList) {
//     //     for (String frameUrl : iFrameUrlList) {
//     //
//     //     }
//     // }
//
//     public static Set<String> getLikeVideoList(String url) throws Exception {
//         logger.info("请求喜欢列表 url:{}", url);
//         String firstLikeHtml = HttpClientCenter.getWebPage(url);
//         Document firstLikeDocument = Jsoup.parse(firstLikeHtml);
//         String title = firstLikeDocument.title();
//         if (TumblrElementConstant.REQUEST_DENIED.equals(title)) {
//             throw new RequestDeniedException("访问该博客的喜欢列表错误未读取到权限");
//         }
//         Set<String> likeVideoList = TumblrHtmlResolve.getVideoUrl(firstLikeHtml);
//         // 循环获取
//         String nextHref = getNextLike(firstLikeDocument);
//         boolean hasNext = nextHref != null;
//         while (hasNext) {
//             nextHref = url + nextHref;
//             String webPage = HttpClientCenter.getWebPage(nextHref);
//             Document nextPage = Jsoup.parse(webPage);
//             logger.info("url:{}", nextHref);
//             likeVideoList.addAll(TumblrHtmlResolve.getVideoUrl(webPage));
//             nextHref = getNextLike(nextPage);
//             hasNext = nextHref != null;
//         }
//         return likeVideoList;
//     }
//
//     private static String getNextLike(Element firstLikeDocument) {
//         String nextHref = null;
//         Element pageElement = firstLikeDocument.getElementById(TumblrElementConstant.DIV_ID_NEXT_PAGE);
//         if (pageElement != null) {
//             nextHref = pageElement.attr(TumblrElementConstant.ATTR_HREF);
//             nextHref = nextHref.substring(nextHref.indexOf("/like/") + "/like/".length() + 1, nextHref.length());
//         }
//         return nextHref;
//     }
//
//     public static void main(String[] args) {
//         String nextHref = "/likes/page/2/1477582735";
//         nextHref.substring(nextHref.indexOf("/like/") + "/like".length(), nextHref.length());
//     }
//
// }
