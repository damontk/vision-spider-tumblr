package com.vision.gyfcat;

import com.alibaba.fastjson.JSONObject;
import com.vision.entity.TumblrVideoEntity;
import com.vision.util.http.util.HttpRequestDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Filename uploadFiled.java
 * @Description
 * @Version 1.0
 * @Author zhongc
 * @Email uuspiders@gmail.com
 * @History <li>Author: zhongc</li>
 * <li>Date: 2017/5/9</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
@Component
public class UploadFiled {

    public static final String VIDEO_UPLOAD_API = "https://api.gfycat.com/v1/gfycats";

    @Resource
    private HttpRequestDao httpRequestDao;

//    @Resource
//    private TokenSingleton tokenSingleton;


    public void upload(TumblrVideoEntity entity) {
        JSONObject jsonObject = new JSONObject();
        // {"fetchUrl":"https://somevideo.somesite.com/video.mp4","title":"This is a title"}
        // jsonObject.put("fetchUrl", entity.getUrl());
        // jsonObject.put("title", entity.getSourceBlogUrl());
        jsonObject.put("fetchUrl", "https://vtt.tumblr.com/tumblr_o46ewySl7K1vn98kj.mp4");
        jsonObject.put("title", "test");
        jsonObject.put("private", true);
        jsonObject.put("noMd5", 1);
//        tokenSingleton.init();
//        try {
//
//            Header authorization = tokenSingleton.getAuthorization();
//            System.out.println(authorization.getName() + ":" + authorization.getValue());
////            String s = httpRequestDao.postRequestJson(VIDEO_UPLOAD_API, jsonObject.toJSONString(), null, tokenSingleton.getAuthorization());
//
////            System.out.println(s);
//        } catch (RequestFailedException e) {
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {
        String src = "www.asdfhaf.com/wejhfasdf.jep";
        String fileName = "";
        if (src.endsWith("/")) {
            src = src.substring(0, src.length() - 1);
        }
        fileName = src.substring(StringUtils.lastIndexOf(src, "/") + 1, src.length());
        int i = StringUtils.lastIndexOf(fileName, ".");
        if (i > -1) {
            fileName = fileName.substring(0, i);
        }
        System.out.println(fileName);
    }
}
