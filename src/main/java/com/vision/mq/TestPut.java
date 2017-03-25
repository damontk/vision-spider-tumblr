package com.vision.mq;

/**
 * 项目名称：vision
 * 类名称： TestPut
 * 类描述：
 * 创建人：zc
 * 创建时间：2017-01-19 15:06
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class TestPut extends Thread {
    private RedisMqPut redisMq;

    public TestPut setRedisMq(RedisMqPut redisMq) {
        this.redisMq = redisMq;
        return this;
    }

    @Override
    public void run() {
        int count = 0;
        while (count < 1000000) {
            count++;
            System.out.println("put:" + count);
            redisMq.putMq(String.valueOf(count));
        }
    }
}
