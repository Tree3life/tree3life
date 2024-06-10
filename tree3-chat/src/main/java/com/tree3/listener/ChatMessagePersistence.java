package com.tree3.listener;

import com.rabbitmq.client.Channel;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.tree3.config.RabbitMQConsumerConfig.RabbitMQElements.TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK;

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
     * 用于配合手动签收 实现重试
     * 《msg标识,重试次数》
     */
    private Map<String, Integer> counterMap = new ConcurrentHashMap<>();

    /**
     * 将聊天消息，异步的存储到数据库中
     * <p>
     * MQ是中间件，只是辅助工具，使用MQ的目的就是解耦和转发，不做多余的事情，保证MQ本身是流畅的、职责单一的即可
     *
     * @param msg     接收到的消息内容，即队列中的消息。
     * @param channel RabbitMQ 的通道（Channel），可以用于进行消息确认、拒绝等操作。
     * @param message 接收到的消息对象，包含了消息的内容和属性等信息。
     */
    @RabbitListener(queues = TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK, ackMode = "MANUAL")
    public void chatMsgWorkQueueConsumer1(String msg, Channel channel, Message message) {
        MessageProperties messageProperties = message.getMessageProperties();
        //获取消息的交付标签（delivery tag），表示消息在通道中的唯一标识。
        long deliveryTag = messageProperties.getDeliveryTag();

        //向数据库中发送消息时需要设置消息的唯一标识
        String msgIdentifier = (String) message.getMessageProperties().getHeaders()
                .get("spring_returned_message_correlation");
        log.debug("getDeliveryTag():{}", messageProperties.getDeliveryTag());
        log.debug("msgIdentifier:{}", msgIdentifier);
        /**
         * 统计重试次数
         */
        Integer retryCount = counterMap.get(msgIdentifier);
        if (retryCount == null) {
            counterMap.put(msgIdentifier, 1);
        } else {
            counterMap.put(msgIdentifier, retryCount.intValue() + 1);
        }

        Integer counter = counterMap.get(msgIdentifier);
        log.info("第{}次消费 消息-{}：{}", counter, msgIdentifier);

        //region 正常消费逻辑
        try {
            MessageText messageText = JSONUtils.paresToObj(msg, MessageText.class);
            // ...
            log.info("chatMsgWorkQueueConsumer1===={},message=={}", msg, message);
            //demo: MessageText(super=MessageHead(createTime=Fri May 24 10:52:23 CST 2024, commandType=11, state=0, deliveryTime=null, from=2, to=1), content=aaa)
            log.warn("messageText:{}", messageText);
            // 将信息存储到数据库中去
//        RabbitTemplate rabbitTemplate = SpringUtil.getBean(RabbitTemplate.class);
            ChatHistory insert = historyService.insert(PojoConvertor.convertTOChatHistory(messageText));
            channel.basicAck(deliveryTag, false);
            //endregion 正常消费逻辑

        } catch (Exception e) {

            try {
                //三次重试失败后，不再重回队列
                //region 出现异常后的消费逻辑
                log.error("message:{}-第 {} 次重试，消费端收到的消息：{},异常原因：{}", msgIdentifier, counter, message, e.getMessage());
                if (counter >= 3) {
                    //三次重试失败后 签收该消息 并对该消息进行消息补偿
                    // optimized：将处理过程中产生异常的消息 转发到 死信队列进行手动 消息补偿 (Rupert，2024/6/10 )
                    //  在死信队列里消费该消息前应当先检查 该消息是否 已经被消费过，预防消息的重复消费
                    //  此处的处理方式有：
                    //  1.将消息放入死信队列
                    //  2.将消息存储到数据库中
                    //  3.将消息使用sse获取websocket推送给用户（可以结合 spring的事件发布 功能发布出去）
                    log.error("消息被添加到死信队列中：{}", message);
                    channel.basicAck(deliveryTag, false);

                    counterMap.remove(msgIdentifier);
                } else {
                    //重试
                    channel.basicNack(deliveryTag, false, true);
                }

                //endregion 异常消费逻辑
            } catch (IOException ioException) {
                log.error("重试/异常签收过程中出现消息异常：{}", message);
                ioException.printStackTrace();
            }
        }
//        finally {
//            //region 签收
//            try {
//                channel.basicAck(deliveryTag, false);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //endregion 签收
//        }
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
