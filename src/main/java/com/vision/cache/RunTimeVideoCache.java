package com.vision.cache;

import com.google.common.collect.Lists;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author zhongc
 * @version 1.0
 * @Filename RunTimeVideoCache.java
 * <p>
 * Description
 * @Email zhong_ch@foxmail.com
 * @History <li>Author: zhongc</li>
 * <li>Date: 2017/2/19</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
@Component
public class RunTimeVideoCache {
    @Resource
    private VideoCache cache;

    /**
     * 随机获取视频url
     *
     * @param count 获取数量
     * @return
     */
    public List<String> getSome(int count) {
        List<String> videoUrl = Lists.newArrayList();
        Set<String> allVideo = cache.getAllKey();
        int size = allVideo.size();
        Object[] objects = allVideo.toArray();
        if (size > 0) {
            for (int i = 0; i < count; i++) {
                int random = RandomUtils.nextInt(size);
                String key = (String) objects[random];
                String url = key.substring(key.indexOf(".com/") + 5, key.length());
                videoUrl.add(url);
            }
        }
        return videoUrl;
    }
}
