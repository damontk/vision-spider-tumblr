package com.vision.mq;

import com.vision.util.redis.single.RedisDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhongc
 * @version 1.0
 * @Filename RedisMq.java
 * <p>
 * Description
 * @Email uusipders@gmail.com
 * @History <li>Author: zhongc</li>
 * <li>Date: 2017/1/17</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
@Component
public class RedisMqPut {
    private static final String BLOG_CACHE_MQ = "tumblr:blog:mq";

    // @Resource
    // private JedisPool jedisPool;
    //
    // private Jedis jedis;
    //
    //
    // @PostConstruct
    // public void init() {
    //     jedis = jedisPool.getResource();
    // }

    @Resource
    private RedisDao redisDao;

    /**
     * 插入
     *
     * @param blogUrl
     */
    public void putMq(String blogUrl) {
        redisDao.lpush(BLOG_CACHE_MQ, blogUrl);
    }
}
