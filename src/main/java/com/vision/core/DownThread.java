package com.vision.core;

import com.google.common.io.Files;
import com.google.common.net.MediaType;
import com.vision.constant.DownStatusEnum;
import com.vision.entity.TumblrVideoEntity;
import com.vision.util.http.HttpRequestDao;
import com.vision.util.http.exception.FileTypeException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

/**
 * 项目名称：com.zhongc
 * 类名称： DownThread
 * 类描述：
 * 创建人：zc
 * 创建时间：2017-01-04 23:51
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0
 */
public class DownThread implements Callable<TumblrVideoEntity> {

    private static final Logger logger = LoggerFactory.getLogger(DownThread.class);

    private static final String DOWNING_FILE_TYPE = "downing";


    /**
     * 二级目录地址
     */
    private String downPath;

    /**
     * 视频url
     */
    private String url;

    /**
     * 视频实体
     */
    private TumblrVideoEntity videoEntity;

    private HttpRequestDao httpRequestDao;

    public DownThread(TumblrVideoEntity videoEntity, String downPath, HttpRequestDao httpRequestDao) {
        this.videoEntity = videoEntity;
        this.downPath = downPath;
        this.url = videoEntity.getUrl();
        this.httpRequestDao = httpRequestDao;
    }

    @Override
    public TumblrVideoEntity call() throws IOException {
        InputStream content = null;
        try {
            logger.info("下载文件地址:{}", url);
            CloseableHttpResponse response = httpRequestDao.getResponse(url);
            // String fileName = url;
            String fileName = url.substring(url.lastIndexOf("/tumblr_") + 1, url.length());
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            content = entity.getContent();
            if (statusCode == HttpStatus.SC_OK) {
                this.generateFile(content, entity.getContentType(), fileName);
            } else {
                logger.error("下载错误 url:{} status:{}", url, statusCode);
                videoEntity.setStatusEnum(DownStatusEnum.FILED_UNDEFINED);
            }
        } catch (SocketTimeoutException e) {
            logger.error("下载过程中出socket 超时 :url:{}", url, e);
            videoEntity.setStatusEnum(DownStatusEnum.NET_TIMEOUT);
        } catch (FileTypeException e) {
            logger.error("出现非视频文件的格式类型:url:{}", url, e);
            videoEntity.setStatusEnum(DownStatusEnum.NET_FILED);
        } catch (IOException e) {
            logger.error("下载过程中出现错误或截取文件时异常:url:{}", url, e);
            videoEntity.setStatusEnum(DownStatusEnum.NET_FILED);
        } catch (Exception e) {
            logger.error("下载过程中出现未知异常:url:{}", url, e);
            videoEntity.setStatusEnum(DownStatusEnum.UNKNOWN_FILED);
        } finally {
            if (content != null) {
                content.close();
            }
        }
        videoEntity.setStatusEnum(DownStatusEnum.SUCCESS);
        return videoEntity;
    }

    private void generateFile(InputStream content, Header contentType, String fileName) throws IOException, FileTypeException {
        //
        String downingName = fileName.substring(0, fileName.indexOf(".") + 1) + DOWNING_FILE_TYPE;
        File file = new File(downPath, downingName);
        File realFile = new File(downPath, fileName);
        if (realFile.exists()) {
            logger.warn("存在文件 直接跳过下载... 文件名称:{}", realFile.getName());
            return;
        }
        Files.createParentDirs(file);
        System.out.println("创建文件:" + fileName);
        try (FileOutputStream outputStream = new FileOutputStream(file, true)) {
            String contentTypeValue = contentType.getValue();
            if (!MediaType.parse(contentTypeValue).is(MediaType.ANY_VIDEO_TYPE)) {
                throw new FileTypeException("准备下载的文件类型错误:" + contentTypeValue);
            }
            if (contentTypeValue.equals(MediaType.MP4_VIDEO.toString())) {
                this.writeFile(content, outputStream);
            } else {
                logger.warn("奥 出现了新的视频文件类型：url{},类型:{}", url, contentTypeValue);
                this.writeFile(content, outputStream);
            }
            outputStream.close();
            this.renameFile(file, realFile);
            logger.info("文件下载完成:{}", realFile.getName());
        }
    }

    /**
     * 写入文件
     *
     * @param content      inputStream
     * @param outputStream 写入文件流
     * @throws IOException
     */
    private void writeFile(InputStream content, FileOutputStream outputStream) throws IOException {
        byte[] bytes = new byte[1024];
        int b;
        while ((b = content.read(bytes)) != -1) {
            outputStream.write(bytes, 0, b);
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFile 原文件
     * @param newFile 新文件
     */
    private void renameFile(File oldFile, File newFile) {
        //设置文件结束
        try {
            Files.move(oldFile, newFile);
        } catch (IOException e) {
            logger.info("重命名文件失败:源文件名称:{}", oldFile.getName());
        }

    }
}
