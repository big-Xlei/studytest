package com.xl.study.studytest.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * Copyright (C),2021/3/1 ,脉策科技杭州团队
 *
 * @ description:OSS上传下载功能
 * @ author:wangjie
 * @ data:2021/3/1 10:28
 */
@Slf4j
@Component
public class OSSUtil {


    @Value("${oss.ossEndpoint}")
    private String ossEndpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;

    /**
     * OSS流式上传URL
     */
    public String upload(MultipartFile file, InputStream is) {
        String objectName = "";
        OSS ossClient = new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);
        try {
            String fileName = file.getOriginalFilename();
            objectName = UUID.randomUUID().toString().replace("-","").substring(0, 15) + "_" + fileName;
//            InputStream ips = file.getInputStream();
            ossClient.putObject(bucketName, objectName, is);
        } catch (Exception e) {
            log.info(file.getOriginalFilename() +"文件上传失败");
            throw new RuntimeException(e.toString());
        } finally {
            // 关闭流
            ossClient.shutdown();
        }
        return objectName;
    }

    /**
     * OSS流式下载
     */
    public File download(String objectName,String suffix) {
        String fileName = objectName.substring(0,objectName.lastIndexOf("."));
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BufferedOutputStream stream = null;
        InputStream inputStream = null;
        File file = null;
        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);
        try {
            OSSObject ossObject = ossClient.getObject(bucketName, objectName);
            inputStream = ossObject.getObjectContent();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            file = File.createTempFile("file", suffix);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fileOutputStream);
            stream.write(outStream.toByteArray());

        } catch (Exception e) {
            log.error(e.toString());
        } finally {
            // 关闭流
            ossClient.shutdown();
            try {
                if (inputStream != null)
                    inputStream.close();
                if (stream != null)
                    stream.close();
                outStream.close();
            } catch (Exception ignored) {
            }
        }
        return file;
    }
}
