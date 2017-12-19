package com.mine.oa;

import com.mine.oa.job.TimedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
public class TimerJob {

    @Resource(name = "removeImgJob")
    private TimedTask removeImgJob;

    @Scheduled(cron = "0 30 1 * * ?")
    public void removeImg() {
        removeImgJob.run();
    }

}
