package com.mine.oa.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mine.oa.service.SimulateSendMsgService;
import com.mine.oa.util.TaskSplitAction;

/***
 *
 * 〈模仿消息发送〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Service
public class SimulateSendMsgServiceImpl implements SimulateSendMsgService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulateSendMsgServiceImpl.class);

    private static final BlockingQueue<Collection<Integer>> BLOCKING_QUEUE = new LinkedBlockingQueue<>();

    @Autowired
    private ForkJoinPool forkJoinPoool;

    @Override
    public void putMsgToQueue() {
        try {
            lockDataIp();
            Multimap<Integer, Integer> dataMap = findMsg();
            Map<Integer, Collection<Integer>> map = dataMap.asMap();
            for (Map.Entry<Integer, Collection<Integer>> msgListEntry : map.entrySet()) {
                BLOCKING_QUEUE.put(msgListEntry.getValue());
            }
            LOGGER.info("本次共推送{}次消息至BLOCKING_QUEUE中", map.size());
        } catch (Exception e) {
            LOGGER.error("putMsgToQueue error", e);
        }

    }

    @Override
    public void sendMsgFromQueue() {
        try {
            Collection<Integer> dataList;
            int i = 0;
            while ((dataList = BLOCKING_QUEUE.poll(2, TimeUnit.SECONDS)) != null) {
                i++;
                LOGGER.info("准备发送{}条消息", dataList.size());
                Thread.sleep(200);// 模拟调用发送接口+保存发送结果
            }
            LOGGER.info("本次共推送{}次消息至消息接口", i);
        } catch (Exception e) {
            LOGGER.error("BLOCKING_QUEUE消费异常", e);
        }
    }

    private void lockDataIp() throws InterruptedException {
        Thread.sleep(200);// 模拟数据等新ip
        LOGGER.debug("数据ip更新完成");
    }

    private Multimap<Integer, Integer> findMsg() throws Exception {
        List<Integer> list = Lists.newArrayList();
        for (int i = 0; i < 1000; i++) {
            list.add(RandomUtils.nextInt(1, 10000));
        }
        // 并发判断是否限制发送
        // forkJoinPoool.invoke为同步执行，execute和submit为异步，RecursiveAction使用submit后的返回值无法使用
        List<Integer> limitMsgList = Lists.newCopyOnWriteArrayList();
        forkJoinPoool.invoke(new TaskSplitAction<>(list, 0, list.size(), msg -> {
            try {
                Thread.sleep(5);// 模拟判断是否限制发送
                if (msg < 100) {
                    // LOGGER.info("{}限制推送", msg);
                    limitMsgList.add(msg);
                }
            } catch (Exception e) {
                LOGGER.warn("判断是否限制发送异常", e);
            }
        }));
        // 阻塞当前线程必须要执行后才能继续
        Multimap<Integer, Integer> dataMap = ArrayListMultimap.create();
        for (Integer msg : list) {
            if (limitMsgList.contains(msg)) {
                continue;
            }
            // Thread.sleep(5);// 模拟判断是否限制发送
            dataMap.put(msg % 2, msg);
        }
        Thread.sleep(100);// 模拟更新为运行状态
        return dataMap;
    }

}
