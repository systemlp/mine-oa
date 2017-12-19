package com.mine.oa.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Component
public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    @Value("${file.path.img}")
    private String imgPath;

    public String uploadImg(MultipartFile imgFile) {
        try {
            String fileName = UUID.randomUUID().toString()
                    + imgFile.getOriginalFilename().substring(imgFile.getOriginalFilename().lastIndexOf("."));
            String imgUrl = imgPath + fileName;
            FileOutputStream out = new FileOutputStream(imgUrl);
            out.write(imgFile.getBytes());
            out.flush();
            out.close();
            return imgUrl;
        } catch (IOException e) {
            LOGGER.error("图片上传失败", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static byte[] getImg(String imgUrl) {
        try {
            byte[] data;
            InputStream inputStream = new FileInputStream(imgUrl);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            return data;
        } catch (IOException e) {
            LOGGER.error("图片读取异常", e);
            throw new RuntimeException(e);
        }
    }

    public static String getImgBase64(String imgUrl) {
        String imgType = imgUrl.substring(imgUrl.lastIndexOf(".") + 1);
        return String.format("data:image/%s;base64,%s", imgType,
                new BASE64Encoder().encode(getImg(imgUrl)).replaceAll("\r\n", ""));
    }

}
