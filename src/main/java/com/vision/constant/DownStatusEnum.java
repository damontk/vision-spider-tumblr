package com.vision.constant;

/**
 * 项目名称：vision
 * 类名称： DownStatusEnum
 * 类描述：
 * 创建人：zc
 * 创建时间：2017-01-05 11:04
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public enum DownStatusEnum {

    /**
     * 成功
     */
    SUCCESS(1, "success"),

    NET_TIMEOUT(6,"timeout"),

    /**
     * 超时
     */
    NET_FILED(2, "net_error"),

    /**
     * 未知错误
     */
    UNKNOWN_FILED(3, "unknown_error"),

    /**
     * 文件操作错误
     */
    FILE_FAILED(4, "file_error"),

    /**
     *
     */
    FILED_UNDEFINED(5, "filed_undefined");

    private int code;
    private String desc;

    DownStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
