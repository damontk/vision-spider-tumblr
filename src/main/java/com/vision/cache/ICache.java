package com.vision.cache;

import java.util.Map;

/**
 * 项目名称：vision
 * 类名称： ICache
 * 类描述：
 * 创建人：zc
 * 创建时间：2016-12-28 11:36
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public interface ICache<T> {

    /**
     * --
     * 放入缓存
     *
     * @param key   key
     * @param value value
     * @return true 成功 false 已存在
     */
    boolean put(String key, T value);

    /**
     * 取值
     *
     * @param key key
     * @return value
     */
    T getValue(String key);

    /**
     * key是否存在
     *
     * @param key key
     * @return true 存在  false 不存在
     */
    boolean containsKey(String key);

    /**
     * 获取所有缓存
     *
     * @return
     */
    Map<String, T> getAll();

    /**
     * 移除缓存
     *
     * @param key key
     * @return 是否成功
     */
    boolean remove(String key);

    /**
     * 移除所有
     *
     * @return 是否成功
     */
    void removeAll();
}
