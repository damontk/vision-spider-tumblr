package com.vision.gyfcat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vision.util.http.exception.RequestFailedException;
import com.vision.util.http.util.HttpRequestDao;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Filename GyfcatToken.java
 * @Description
 * @Version 1.0
 * @Author zhongc
 * @Email zhong_ch@foxmail.com
 * @History <li>Author: zhongc</li>
 * <li>Date: 2017/5/9</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
@Component
public class TokenSingleton {

    private Header Authorization;

    @Resource
    private HttpRequestDao httpRequestDao;

    // @PostConstruct
    public void init() {
        JSONObject json = new JSONObject();
        json.put("grant_type", "client_credentials");
        json.put("client_id", "2_aqslM_");
        json.put("client_secret", "yIApYmcK_RZGqIw1Pq-MLpMSqjSjVQ355WAr8lcOyfMUvt8iNBdoRM5QeBMWSCfP");
        try {

            String result = httpRequestDao.postRequestJson("https://api.gfycat.com/v1/oauth/token", json.toJSONString(), null);
            JSONObject jsonObject = JSON.parseObject(result);
            String access_token = jsonObject.getString("access_token");
            BasicHeader header = new BasicHeader("Authorization", "Bearer " + access_token);
            this.setAuthorization(header);
        } catch (RequestFailedException e) {
            e.printStackTrace();
        }
    }

    public Header getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(Header authorization) {
        Authorization = authorization;
    }
}
