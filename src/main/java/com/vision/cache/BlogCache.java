package com.vision.cache;

import com.google.common.collect.Maps;
import com.vision.entity.TumblrBlogEntity;
import com.vision.util.data.MapHelper;
import com.vision.util.redis.single.RedisDao;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

/**
 * 项目名称：com.zhongc
 * 类名称： BlogCache
 * 类描述：
 * 创建人：zc
 * 创建时间：2016-12-27 23:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
@Component
public class BlogCache extends AbsCache<TumblrBlogEntity> {


    // private Jedis cluster;

    @Resource
    private RedisDao cluster;

    private static final String BLOG_CACHE = "tumblr.blog.cache.";

    // @PostConstruct
    // public BlogCache(JedisPool jedisPool) {
    //     this.cluster = jedisPool.getResource();
    //     super.AbsCache(cluster);
    // }


    @Override
    public boolean put(String key, TumblrBlogEntity value) {
        return super.put(this.genKey(key), value);
    }

    @Override
    public TumblrBlogEntity getValue(String key) {
        Map<String, String> map = cluster.hgetAll(this.genKey(key));
//        TumblrBlogEntity blogEntity = new TumblrBlogEntity();
        return new MapHelper<TumblrBlogEntity, String, String>().putMapToBeans(map, TumblrBlogEntity.class);
    }

    @Override
    public boolean containsKey(String key) {

        return super.containsKey(this.genKey(key));
    }

    @Override
    public Map<String, TumblrBlogEntity> getAll() {
        Map<String, TumblrBlogEntity> blogCaches = Maps.newHashMap();
        Set<String> allKeys = cluster.keys(BLOG_CACHE + "*");
        if (CollectionUtils.isNotEmpty(allKeys)) {
            for (String key : allKeys) {
                Map<String, String> map = cluster.hgetAll(key);
                TumblrBlogEntity blogEntity = new MapHelper<TumblrBlogEntity, String, String>().putMapToBeans(map, TumblrBlogEntity.class);
                blogCaches.put(key, blogEntity);
            }

        }
        return blogCaches;
    }

    @Override
    public boolean remove(String key) {
        return cluster.del(this.genKey(key)) > 0;
    }

    @Override
    public void removeAll() {

    }

    private String genKey(String key) {
        Assert.notNull(key, "key不能为空");
        if (!key.startsWith(BLOG_CACHE)) {
            key = BLOG_CACHE + key;
        }
        return key;
    }
}
