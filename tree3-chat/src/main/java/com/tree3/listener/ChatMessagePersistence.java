package com.tree3.listener;

import com.rabbitmq.client.Channel;
import com.tree3.config.RabbitMQConsumerConfig;
import com.tree3.pojo.PojoConvertor;
import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.message.MessageText;
import com.tree3.service.ChatHistoryService;
import com.tree3.utils.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 将聊天消息持久化到数据库（在MQ中扮演消费者角色），起到 `异步削峰` 的作用
 * 由于聊天内容是个十分调用频繁的接口，所以采用以下策略对消息进行处理：
 * 1.将聊天消息 投递到 MQ的队列中
 * 2.在本类中对 MQ队列里的消息 向数据库持久化
 * （两个客户端同时向 数据库写入聊天消息）
 *
 * @Author: Jinhui
 * @Date 2022/10/16 17:33
 */
@Slf4j
@Component
public class ChatMessagePersistence {

    @Resource
    private ChatHistoryService historyService;

    /**
     * 将聊天消息，异步的存储到数据库中
     *
     * @param msg     接收到的消息内容，即队列中的消息。
     * @param channel RabbitMQ 的通道（Channel），可以用于进行消息确认、拒绝等操作。
     * @param message 接收到的消息对象，包含了消息的内容和属性等信息。
     */
    @RabbitListener(queues = RabbitMQConsumerConfig.TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK, ackMode = "MANUAL")
// ackMode = "MANUAL"开启手动确认模式
    public void chatMsgWorkQueueConsumer1(String msg, Channel channel, Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        //获取消息的交付标签（delivery tag），表示消息在通道中的唯一标识。
        long deliveryTag = messageProperties.getDeliveryTag();

        try {
            MessageText messageText = JSONUtils.paresToObj(msg, MessageText.class);


            // ...
            log.info("chatMsgWorkQueueConsumer1===={},message=={}", msg, message);
            log.info("--------------------");
//            MessageText(super=MessageHead(createTime=Fri May 24 10:52:23 CST 2024, commandType=11, state=0, deliveryTime=null, from=2, to=1), content=aaa)
//            MessageText(super=MessageHead(createTime=Fri May 24 10:52:23 CST 2024, commandType=11, state=0, deliveryTime=null, from=2, to=1), content=aaa)
            log.warn("messageText:{}", messageText);
            log.info("--------------------");
            //todo 将信息存储到数据库中去
//        RabbitTemplate rabbitTemplate = SpringUtil.getBean(RabbitTemplate.class);
            ChatHistory insert = historyService.insert(PojoConvertor.convertTOChatHistory(messageText));
            channel.basicAck(deliveryTag, true);
        } catch (Exception e) {
            log.error("消费端收到的消息：{}",msg);
            // 记录日志
            log.error("消费端签收消息时发生异常：{}", e.getMessage());
            try {
                channel.basicNack(deliveryTag, true, true);
            } catch (IOException ioException) {
                log.info("丢弃消息异常!被丢弃的消息{}", msg);
            }
        }
    }


    /**
     * 将聊天消息，异步的存储到数据库中
     *
     * @param msg     接收到的消息内容，即队列中的消息。
     * @param channel RabbitMQ 的通道（Channel），可以用于进行消息确认、拒绝等操作。
     * @param message 接收到的消息对象，包含了消息的内容和属性等信息。
     */
//    @RabbitListener(queuesToDeclare = @Queue(RabbitMQConsumerConfig.TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK), ackMode = "MANUAL")
//    public void chatMsgWorkQueueConsumer2(String msg, Channel channel, Message message) throws InterruptedException {
//        MessageProperties messageProperties = message.getMessageProperties();
//        //获取消息的交付标签（delivery tag），表示消息在通道中的唯一标识。
//        long deliveryTag = messageProperties.getDeliveryTag();
//
//        //获取消息的唯一标识（cluster ID）
//        String correlationId = messageProperties.getCorrelationId();
//        try {
//            Thread.sleep(1000);
//            //region 处理消息的业务
//            log.info("chatMsgWorkQueueConsumer2===={},message=={}", msg, message);
//            //endregion处理消息的业务
//
//            //手动签收，告知 RabbitMQ 已成功处理该消息。
//            // multiple:是否批量处理（true:一次性ack所有小于deliveryTag的消息;false：仅确认当前的消息。）
//            channel.basicAck(deliveryTag, true);
//        } catch (IOException e) {
//            log.error("消息未签收！！", e);
//            try {
//                //拒绝签收;
//                // multiple：是否批量处理（true 表示将一次性ack所有小于deliveryTag的消息）
//                // requeue:重回队列。如果设置为true,则消息重新回到queue,broker会重新发送该消息给消费端
//                channel.basicNack(deliveryTag, true, true);
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//        }
//    }
}
