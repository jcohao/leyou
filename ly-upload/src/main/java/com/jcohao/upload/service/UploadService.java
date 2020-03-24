package com.jcohao.upload.service;


import com.jcohao.upload.controller.UploadController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
public class UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    public String upload(MultipartFile file) {
        // 图片信息校验，文件大小限制已经在 Spring 配置中解决
        // 校验文件类型
        String type = file.getContentType();
        if (!suffixes.contains(type)) {
            logger.info("上传失败，文件类型不匹配：{}", type);
            return null;
        }
        // 保存图片
        // 生成保存目录
        File dir = new File("H:\\leyou\\upload");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存
        try {
            file.transferTo(new File(dir, file.getOriginalFilename()));
        } catch (IOException e) {
            logger.error("保存失败，返回为空");
            e.printStackTrace();
            return null;
        }
        // 一般静态资源都应该使用独立域名
        // 这样访问静态资源时不会携带不必要的 cookie，从而减少请求的数据量
        String url = "http://image.leyou.com/upload/" + file.getOriginalFilename();
        logger.info("上传成功，url：{}", url);
        return url;
    }
}
