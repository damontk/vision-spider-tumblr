package com.vision.cache;

import com.google.common.collect.Maps;
import com.vision.entity.TumblrVideoEntity;
import com.vision.util.data.MapHelper;
import com.vision.util.redis.single.RedisDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：vision
 * 类名称： VideoCache
 * 类描述：
 * 创建人：zc
 * 创建时间：2016-12-28 11:59
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
@Component
public class VideoCache extends AbsCache<TumblrVideoEntity> {

    @Resource
    private RedisDao cluster;

    static final String VIDEO_CACHE = "tumblr.videocache";


    @Override
    public boolean put(String key, TumblrVideoEntity value) {
        return super.put(this.genKey(key), value);
    }

    @Override
    public TumblrVideoEntity getValue(String key) {
        Map<String, String> map = cluster.hgetAll(this.genKey(key));
        return new MapHelper<TumblrVideoEntity, String, String>().putMapToBeans(map, TumblrVideoEntity.class);
    }

    @Override
    public boolean containsKey(String key) {
        return super.containsKey(this.genKey(key));
    }

    @Override
    public Map<String, TumblrVideoEntity> getAll() {
        Map<String, TumblrVideoEntity> videoCaches = Maps.newHashMap();
        Set<String> allKeys = cluster.keys(VIDEO_CACHE + "*");
        if (CollectionUtils.isNotEmpty(allKeys)) {
            for (String key : allKeys) {
                Map<String, String> map = cluster.hgetAll(key);
                TumblrVideoEntity videoEntity = new MapHelper<TumblrVideoEntity, String, String>().putMapToBeans(map, TumblrVideoEntity.class);
                videoCaches.put(key, videoEntity);
            }

        }
        return videoCaches;
    }

    public Set<String> getAllKey() {
        Map<String, TumblrVideoEntity> videoCaches = Maps.newHashMap();
        return cluster.keys(VIDEO_CACHE + "*");
    }

    @Override
    public boolean remove(String key) {
        return false;
    }

    @Override
    public void removeAll() {
    }

    private String genKey(String key) {
        Assert.notNull(key, "key不能为空");
        if (!key.startsWith(VIDEO_CACHE)) {
            key = VIDEO_CACHE + key;
        }
        return key;
    }
}
