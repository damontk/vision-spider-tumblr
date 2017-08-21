// package com.vision.core;
//
// import com.vision.constant.TumblrUrlConstant;
// import com.vision.util.http.exception.LoginFailedException;
// import com.vision.util.http.util.HttpRequestDao;
// import org.apache.commons.lang.StringUtils;
// import org.apache.http.Header;
// import org.apache.http.message.BasicHeader;
// import org.springframework.stereotype.Component;
//
// import javax.annotation.Resource;
// import java.util.Map;
//
// /**
//  * @author zhongchao
//  * @title
//  * @date 2017-08-18
//  * @since v1.0.0
//  */
//
// @Component
// public class Login {
//     @Resource
//     private HttpRequestDao tumblrHttpRequestDao;
//
//
//     /**
//      * 验证密码
//      *
//      * @param userName 用户名
//      * @param pwd      密码
//      * @param param    form表单参数
//      * @throws LoginFailedException 密码错误 抛出异常
//      */
//     public void validatePwd(String userName, String pwd, Map<String, String> param, String cookiePath, Header... headers) throws Exception {
//         //执行登录操作
//         LoginFormParams(param, true, userName, pwd);
// //        String cookie = "tmgioct=5996a2ea9266d50833737770; _ga=GA1.2.1779491099.1503044334; _gid=GA1.2.429192934.1503044334; __utma=189990958.1779491099.1503044334.1503044334.1503044372.2; __utmb=189990958.0.10.1503044372; __utmc=189990958; __utmz=189990958.1503044334.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); rxx=1asuf61ghaj.tpb6ry0&v=1; pfl=ZTgzZGNjNmQyZmI4ZTk4MTE2MTczMTU1MjEzNWM5YjQyNjViYzVmY2Q2YzQwMTkwYTFkMzk2YWU2NTRkMTQ2MSxuemN6djkzbWs1OXR3dDlhdXZ4NmU5ZjRtM3hjdmJtYiwxNTAzMDUxMDU1; devicePixelRatio=undefined; documentWidth=1920; yx=9x00prcpcq8as%26o%3D3%26f%3Dv6; anon_id=OEJBLYMCMJQGLVDGIOMJVKQILMVZDXUG";
// //        BasicHeader header = new BasicHeader("Cookie", cookie);
//         //执行登录操作
//         String loginResult = tumblrHttpRequestDao.postRequest(TumblrUrlConstant.TUMBLR_LOGIN_URL, param, headers);
//
// //        // 登录成功 tumblr status为 302 无返回数据
//         if (!"".equals(loginResult)) {
//             throw new LoginFailedException("密码错误!");
//         }
// //        logger.info("用户:{}登录成功!", userName);
//         if (StringUtils.isNotBlank(cookiePath)) {
//             tumblrHttpRequestDao.serializeObject(tumblrHttpRequestDao.getHttpClientContext().getCookieStore(), cookiePath);
//         }
//         //登陆成功后 跳转登陆成功后的网站
//         //等待
//         Thread.sleep(3);
//         tumblrHttpRequestDao.getWebPage(TumblrUrlConstant.TUMBLR_DASHBOARD);
//         //存 cookie
//     }
//
//     /**
//      * 如果为正式登陆 需要移除几个不需要的参数
//      *
//      * @param param    页面form表单参数
//      * @param login    是否为正式登录
//      * @param userName 用户名
//      * @param pwd      密码
//      */
//     private void LoginFormParams(Map<String, String> param, boolean login, String userName, String pwd) {
//         param.put("tracking_url", "/login");
//         param.put("tracking_version", "modal");
//         param.put("user[email]", login ? userName : "");
//         param.put("user[password]", login ? pwd : "");
//         param.put("tumblelog[name]", "");
//         param.put("user[age]", "");
//         param.put("http_referer", "https://www.tumblr.com/");
//         if (login) {
//             param.remove("action");
//             param.remove("tracking_url");
//             param.remove("tracking_version");
//             param.remove("context");
//             param.put("context", "other");
//         }
//     }
// }
