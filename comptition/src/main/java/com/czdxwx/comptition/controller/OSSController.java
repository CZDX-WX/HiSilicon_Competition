package com.czdxwx.comptition.controller;
import com.czdxwx.comptition.entity.Base64File;
import com.czdxwx.comptition.entity.ComFile;
import com.czdxwx.comptition.service.OSSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/oss")
public class OSSController {

    @Autowired
    private OSSService ossService;



    /**
     * 上传文件
     *
     * @param base64File
     * @return
     */
    @PostMapping(value = "/uploadBase64")
    public String uploadFiles(@RequestBody Base64File base64File) {
        return ossService.uploadBase64(base64File);
    }


    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/uploadFiles")
    public String uploadFiles(@RequestParam("file") MultipartFile file,   @RequestParam("storagePath") String storagePath) {
        return ossService.uploadFile(file, storagePath);
    }

    /**
     * 下载文件
     *
     * @param comFile
     */
    @PostMapping(value = "/exportFile")
    public String exportFile(@RequestBody ComFile comFile) {
        System.out.println(comFile.getFileName());
        return ossService.exportFileAsBase64(comFile.getFileName());
    }

    /**
     * 删除文件
     *
     * @param comFile
     */
    @PostMapping(value = "/deleteFile")
    public void deleteFile(@RequestBody ComFile comFile) {
        ossService.deleteFile(comFile.getFileName());
    }

    /**
     * 查看文件列表
     *
     * @return
     */
    @PostMapping(value = "/listObjects")
    public List<String> listObjects() {
        return ossService.listObjects();
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     */
    @PostMapping(value = "/doesObjectExist")
    public boolean doesObjectExist(@RequestParam("fileName") String fileName) {
        return ossService.doesObjectExist(fileName);
    }

    /**
     * 获取 url
     *
     * @param fileName
     * @param expSeconds
     */
    @PostMapping(value = "/getSingeNatureUrl")
    public String getSingeNatureUrl(@RequestParam("fileName") String fileName, @RequestParam("expSeconds") int expSeconds) {
        return ossService.getSingeNatureUrl(fileName, expSeconds);
    }

    /**
     * 设置文件访问权限
     *
     * @param fileName
     */
    @PostMapping(value = "/setObjectAcl")
    public void setObjectAcl(@RequestParam("fileName") String fileName) {
        ossService.setObjectAcl(fileName);
    }
}
