package com.pinyougou.shop.controller;

import entity.Result;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.FastDFSClient;

@RestController
@RequestMapping("upload")
public class uploadController {

    @Value("${image_server_ip}")
    private String imageServerIp;

    @RequestMapping("uploadFile")
    public Result uploadFile(MultipartFile file) {
        try {
            //加载配置文件
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
            //得到扩展名
            String originalFilename = file.getOriginalFilename();
            //通过截取获得扩展名  例如：jpg,gif
            String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //上传图片
            String uploadFile = fastDFSClient.uploadFile(file.getBytes(), substring);
            //返回结果集，返回的结果是图片的 “链接”
            return new Result(true, imageServerIp + uploadFile);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }
    }
}
