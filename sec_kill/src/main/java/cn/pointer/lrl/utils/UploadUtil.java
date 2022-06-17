package cn.pointer.lrl.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class UploadUtil {

    public static String uploadImage(MultipartFile file, String path) {
        if (file.isEmpty()) return null;
        // 以系统时间戳为文件名
        String url = String.valueOf(System.currentTimeMillis());
        // 获取上传文件后缀名
        String contentType = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        // 文件上传路径
        String filePath = "/" + path + "static/img/";
        File dir = new File(filePath);
        if (!dir.isDirectory()) dir.mkdir();
        File dest = new File(filePath + url + "." + contentType);
        try {
            file.transferTo(dest);
            return url + "." + contentType;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
