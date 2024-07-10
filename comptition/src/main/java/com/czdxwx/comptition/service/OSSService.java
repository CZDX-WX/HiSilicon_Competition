package com.czdxwx.comptition.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.czdxwx.comptition.config.OSSConfiguration;
import com.czdxwx.comptition.entity.Base64File;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OSSService {

    public static Logger log = LoggerFactory.getLogger(OSSService.class);

    @Autowired
    private OSSConfiguration ossConfiguration;

    @Autowired
    private OSS ossClient;

    /**
     * 上传文件到阿里云 OSS 服务器
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
     *
     * @param file
     * @param storagePath
     * @return
     */
    public String uploadFile(MultipartFile file, String storagePath) {
        String fileName = "";
        try {
            // 创建一个唯一的文件名，类似于id，就是保存在OSS服务器上文件的文件名
            fileName = UUID.randomUUID().toString();
            InputStream inputStream = file.getInputStream();
            // 设置对象
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置数据流里有多少个字节可以读取
            objectMetadata.setContentLength(inputStream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            fileName = storagePath + "/" + fileName;
            log.info("文件名为:{}",fileName);
            // 上传文件
            String s = String.valueOf(ossClient.putObject(ossConfiguration.getBucketName(), fileName, inputStream, objectMetadata));
            log.info("返回值为:{}",s);
        } catch (IOException e) {
            log.error("Error occurred: {}", e.getMessage(), e);
        }
        return fileName;
    }

    public void uploadAudio(File file, String storagePath) {

        // 创建PutObjectRequest对象。
        ossClient.putObject(ossConfiguration.getBucketName(), storagePath, file);
        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);
    }

    public String uploadBase64(Base64File base64File) {
        String fileName = "";
        // 解码 Base64 文件
        byte[] decodedBytes = Base64.getDecoder().decode(base64File.getFile());
        InputStream inputStream = new ByteArrayInputStream(decodedBytes);

        // 设置对象元数据
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置数据流里有多少个字节可以读取
        objectMetadata.setContentLength(decodedBytes.length);
        objectMetadata.setCacheControl("no-cache");
        objectMetadata.setHeader("Pragma", "no-cache");
        objectMetadata.setContentType("image/jpg");
        objectMetadata.setContentDisposition("inline;filename=" + base64File.getFileName());
        fileName = base64File.getStoragePath() + "/" + base64File.getFileName();
        // 上传文件
        String s = String.valueOf(ossClient.putObject(ossConfiguration.getBucketName(), fileName, inputStream, objectMetadata));
        log.info("文件名为:{}",fileName);
        log.info("返回值为:{}",s);
        return fileName;
    }

    /**
     * 下载文件
     * 详细请参看“SDK手册 > Java-SDK > 上传文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/download_object.html?spm=5176.docoss/sdk/java-sdk/manage_object
     *
     * @param objectName
     */
    public String exportFileAsBase64(String objectName) {
        // ossObject 包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流
        OSSObject ossObject = ossClient.getObject(ossConfiguration.getBucketName(), objectName);
        // 读取文件内容
        BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
        } catch (IOException e) {
            log.error("Error occurred: {}", e.getMessage(), e);
            return null;
        } finally {
            try {
                in.close();
                baos.close();
            } catch (IOException e) {
                log.error("Error closing streams: {}", e.getMessage(), e);
            }
        }
        // 转换为 Base64
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * 获取 url
     *
     * @param filename
     * @param expSeconds
     * @return
     */
    public String getSingeNatureUrl(String filename, int expSeconds) {
        Date expiration = new Date(System.currentTimeMillis() + expSeconds * 1000);
        URL url = ossClient.generatePresignedUrl(ossConfiguration.getBucketName(), filename, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * 删除文件
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
     *
     * @param fileName
     */
    public void deleteFile(String fileName) {
        try {
            ossClient.deleteObject(ossConfiguration.getBucketName(), fileName);
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage(), e);
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean doesObjectExist(String fileName) {
        try {
            if (Strings.isEmpty(fileName)) {
                log.error("文件名不能为空");
                return false;
            } else {
                return ossClient.doesObjectExist(ossConfiguration.getBucketName(), fileName);
            }
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查看 Bucket 中的 Object 列表
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
     *
     * @return
     */
    public List<String> listObjects() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(ossConfiguration.getBucketName()).withMaxKeys(200);
        ObjectListing objectListing = ossClient.listObjects(listObjectsRequest);
        List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        return objectSummaries.stream().map(OSSObjectSummary::getKey).collect(Collectors.toList());
    }

    /**
     * 设置文件访问权限
     * 详细请参看“SDK手册 > Java-SDK > 管理文件”
     * 链接：https://help.aliyun.com/document_detail/84838.html
     *
     * @param fileName
     */
    public void setObjectAcl(String fileName) {
        ossClient.setObjectAcl(ossConfiguration.getBucketName(), fileName, CannedAccessControlList.PublicRead);
    }
}
