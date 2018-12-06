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
 * @Email uuspiders@gmail.com
 * @History <li>Author: zhongc</li>
 * <li>Date: 2017/1/17</li>
 * <li>Version: 1.0</li>
 * <li>Content: create</li>
 */
@Component
public class RedisMqGet {
    private static final String BLOG_CACHE_MQ = "tumblr:blog:mq";

    // @Resource
    // private JedisPool jedisPool;
    //
    // private Jedis jedis;

    @Resource
    private RedisDao redisDao;

    // @PostConstruct
    // public void init() {
    //     jedis = jedisPool.getResource();
    // }

    public String getOneBlogUrl() {
        return redisDao.rpop(BLOG_CACHE_MQ);
    }


    // public void setJedisPool(JedisPool jedisPool) {
    //     this.redisDao = jedisPool;
    // }
}
