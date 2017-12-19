package com.mine.oa.job.com.mine.oa.job.impl;

import com.mine.oa.constant.OaConstants;
import com.mine.oa.job.TimedTask;
import com.mine.oa.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

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
public class RemoveImgJob implements TimedTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveImgJob.class);

    @Autowired
    private UserMapper userMapper;
    @Value("${file.path.img}")
    private String imgPath;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void run() {
        long time = System.currentTimeMillis();
        LOGGER.info("图片删除任务开始执行");
        List<String> allPhoto = userMapper.findAllPhoto();
        File imgDir = new File(imgPath);
        if (!imgDir.isDirectory()) {
            LOGGER.error("图片文件目录错误{}", imgPath);
            return;
        }
        File[] imgFiles = imgDir.listFiles();
        boolean isDel;
        assert imgFiles != null;
        for (File imgFile : imgFiles) {
            isDel = true;
            for (String photoPath : allPhoto) {
                if (imgFile.getPath().endsWith(OaConstants.DEFAULT_PHOTO)
                        || StringUtils.replaceAll(imgFile.getPath(), "\\\\", "/").equalsIgnoreCase(photoPath)) {
                    isDel = false;
                    break;
                }
            }
            if (isDel) {
                imgFile.delete();
            }
        }
        LOGGER.info("图片删除任务执行结束，共耗时{}毫秒", System.currentTimeMillis() - time);
    }

}
