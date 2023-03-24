package com.ican.strategy.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.ican.config.properties.OssProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * oss上传策略
 *
 * @author ican
 */
@Slf4j
@Service("ossUploadStrategyImpl")
public class OssUploadStrategyImpl extends AbstractUploadStrategyImpl {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public Boolean exists(String filePath) {
        Boolean isExist = true;
        try {
            getOssClient().doesObjectExist(ossProperties.getBucketName(), filePath);
        } catch (OSSException e) {
            isExist = false;
        } catch (ClientException e) {
            isExist = false;
        }
        return isExist;
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream) {
        OSS ossClient = getOssClient();
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketName(), path + fileName, inputStream);
            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");
            // 调用oss方法上传
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 如果上传成功，则返回200。
            log.info("照片上传状态码{}",result.getResponse().getStatusCode());
        } catch (OSSException oe) {
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.info("Request ID:" + oe.getRequestId());
            log.info("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            log.error("Caught an ClientException, Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return ossProperties.getUrl() + filePath;
    }

    /**
     * 获取ossClient
     *
     * @return {@link OSS} ossClient
     */
    private OSS getOssClient() {
        return new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
    }
}
