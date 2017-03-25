package com.vision.core;

import com.vision.entity.TumblrVideoEntity;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 项目名称：vision
 * 类名称： DownVideoExecutorPool
 * 类描述：
 * 创建人：zc
 * 创建时间：2017-01-06 16:51
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class DownVideoExecutorPool {

    // 下载视频线程  使用线程池
    private ThreadPoolExecutor executorService;

    public DownVideoExecutorPool(int nThreads) {
        executorService = new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }


    public void putDownVideoTask(Callable<TumblrVideoEntity> callable) {
        executorService.submit(callable);
    }

    public int getThreadCount() {
        return ((ThreadPoolExecutor) executorService).getActiveCount();
    }

    public int getNThread() {
        return executorService.getCorePoolSize();
    }
}
