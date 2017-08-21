package com.vision.cache;

import com.vision.util.data.MapHelper;
import com.vision.util.redis.single.RedisDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 项目名称：vision
 * 类名称： AbsCache
 * 类描述：
 * 创建人：zc
 * 创建时间：2017-01-18 13:57
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
@Component
public abstract class AbsCache<T> implements ICache<T> {

    // private String cacheKey;

    @Resource
    private RedisDao cluster;

    @Override
    public boolean put(String key, T value) {
        boolean putSuccess = false;
        if (!this.containsKey(key)) {
            cluster.hmset(key, new MapHelper<T, String, String>().putBeansToMap(value));
            putSuccess = true;
        }
        return putSuccess;
    }

    @Override
    public abstract T getValue(String key);

    @Override
    public boolean containsKey(String key) {
        Map<String, String> cache = cluster.hgetAll(key);
        return cache != null && !cache.isEmpty();
    }

    @Override
    public abstract Map<String, T> getAll();

    @Override
    public boolean remove(String key) {
        return false;
    }

    @Override
    public void removeAll() {

    }
}
