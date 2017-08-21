package com.vision.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.vision.constant.TumblrElementConstant;
import com.vision.constant.TumblrUrlConstant;
import com.vision.util.http.exception.LoginFailedException;
import com.vision.util.http.util.CookieUtil;
import com.vision.util.http.util.HttpRequestDao;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 项目名称：vision
 * 类名称： TumblrLoginHandler
 * 类描述：
 * 创建人：zc
 * 创建时间：2016-12-26 16:10
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
@Component
public class TumblrLoginHandler {

    private static final Logger logger = LoggerFactory.getLogger(TumblrLoginHandler.class);

    private String cookiePath;

    private String userName;

    private String pwd;

    @Resource
    private HttpRequestDao tumblrHttpRequestDao;


    public void TumblrLogin(String userName, String pwd, String cookiePath) throws Exception {
        this.userName = userName;
        this.pwd = pwd;
        this.cookiePath = cookiePath;
        if (!CookieUtil.deserializeCookieStore(tumblrHttpRequestDao.getHttpClientContext(), cookiePath)) {
            this.validateAccount();
        }

    }

    private void validateAccount() throws Exception {
        Map<String, String> param = Maps.newHashMap();
        // 获取 form_key
        String webPage = tumblrHttpRequestDao.getWebPage(TumblrUrlConstant.TUMBLR_LOGIN_URL);
        Header[] headers = tumblrHttpRequestDao.getResponseHeaders();
        String pfl = "";
        String tmgioct = "";
        for (Header header : headers) {
            String name = header.getName();
            String value = header.getValue();
            if (name.equals("Set-Cookie") && value.startsWith("pfl")) {
                pfl = value.substring(value.indexOf("pfl=") + 4, value.length());
            }
            if (name.equals("Set-Cookie") && value.startsWith("tmgioct")) {
                tmgioct = value.substring(value.indexOf("tmgioct=") + "tmgioct=".length(), value.length());
                tmgioct = tmgioct.substring(0, tmgioct.indexOf(";"));
            }
        }
        // 解析html 获取对象参数
        Document parse = Jsoup.parse(webPage);
        Elements loginFormClass = parse.getElementsByClass(TumblrElementConstant.LOGIN_FORM_CLASS);
        Assert.notEmpty(loginFormClass, "汤不热登录方式发生变更,如需帮助请反馈,email:zhong_ch@foxmail.com");
        Element element = loginFormClass.get(0);
        Elements inputs = element.getElementsByTag("input");
        for (Element input : inputs) {
            param.put(input.attr(TumblrElementConstant.INPUT_NAME), input.val());
        }
        param.put(TumblrElementConstant.USER_NAME, userName);
        LoginFormParams(param, false, userName, pwd);
        String cookie = "pfl=" + pfl + "; devicePixelRatio=undefined; documentWidth=1008; yx=9x00prcpcq8as%26o%3D3%26f%3Dv6; anon_id=QWCLDPSQEZKNJCSLQCXPQKUJUHHTBKYV; tmgioct=" + tmgioct + "; _ga=GA1.2.1779491099.1503044334; _gid=GA1.2.1021956581.1503281564; __utma=189990958.1779491099.1503044334.1503044372.1503281564.3; __utmb=189990958.0.10.1503281564; __utmz=189990958.1503044334.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); rxx=1asuf61ghaj.tpb6ry0&v=1; __utmc=189990958";
        BasicHeader header = new BasicHeader("Cookie", cookie);

        String valAccount = tumblrHttpRequestDao.postRequest(TumblrUrlConstant.VALIDATE_ACCOUNT_URL, param, header);
        //转换json
        Assert.notNull(valAccount, "用户名验证出错！tumblr未返回参数");
        JSONObject validateObj = JSON.parseObject(valAccount);
        Assert.notNull(validateObj, "汤不热登录方式发生变更,如需帮助请反馈,email:zhong_ch@foxmail.com");
        JSONArray errors = (JSONArray) validateObj.get(TumblrElementConstant.ERRORS);
        // 如果返回参数有 errors 为 并且不为空 说明用户名验证出错  用户名不存在
        if (CollectionUtils.isNotEmpty(errors)) {
            throw new LoginFailedException("用户名不存在！");
        } else {
            this.validatePwd(userName, pwd, param, cookiePath, header);
            //
        }

    }

    public void validatePwd(String userName, String pwd, Map<String, String> param, String cookiePath, Header... headers) throws Exception {
        //执行登录操作
        LoginFormParams(param, true, userName, pwd);
        //执行登录操作
        String loginResult = tumblrHttpRequestDao.postRequest(TumblrUrlConstant.TUMBLR_LOGIN_URL, param, headers);

//        // 登录成功 tumblr status为 302 无返回数据
        if (!"".equals(loginResult)) {
            throw new LoginFailedException("密码错误!");
        }
//        logger.info("用户:{}登录成功!", userName);
        if (StringUtils.isNotBlank(cookiePath)) {
            CookieUtil.serializeObject(tumblrHttpRequestDao.getHttpClientContext().getCookieStore(), cookiePath);
        }
        //登陆成功后 跳转登陆成功后的网站
        //等待
        Thread.sleep(3);
        tumblrHttpRequestDao.getWebPage(TumblrUrlConstant.TUMBLR_DASHBOARD);
        //存 cookie
    }


    /**
     * 如果为正式登陆 需要移除几个不需要的参数
     *
     * @param param    页面form表单参数
     * @param login    是否为正式登录
     * @param userName 用户名
     * @param pwd      密码
     */
    private void LoginFormParams(Map<String, String> param, boolean login, String userName, String pwd) {
        param.put("tracking_url", "/login");
        param.put("tracking_version", "modal");
        param.put("user[email]", login ? userName : "");
        param.put("user[password]", login ? pwd : "");
        param.put("tumblelog[name]", "");
        param.put("user[age]", "");
        param.put("http_referer", "https://www.tumblr.com/");
        if (login) {
            param.remove("action");
            param.remove("tracking_url");
            param.remove("tracking_version");
            param.remove("context");
            param.put("context", "other");
        }
    }
}
