package com.mine.oa.service;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public interface SimulateSendMsgService {

    /**
     * 消息生产
     */
    void putMsgToQueue();

    /**
     * 消息消费
     */
    void sendMsgFromQueue();

}
