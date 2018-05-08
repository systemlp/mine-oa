package com.mine.oa;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mine.oa.job.TimedTask;
import com.mine.oa.service.SimulateSendMsgService;

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
    @Autowired
    private SimulateSendMsgService simulateSendMsgService;

    @Scheduled(cron = "0 30 1 * * ?")
    public void removeImg() {
        removeImgJob.run();
    }

    @Scheduled(cron = "2/3 * * * * ?")
    public void putMsgToQueue() {
        simulateSendMsgService.putMsgToQueue();
    }

    @Scheduled(cron = "0/30 * * * * ?")
    public void sendMsgFromQueue() {
        simulateSendMsgService.sendMsgFromQueue();
    }

}
