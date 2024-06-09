package com.tree3.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @Description
 * @Author: Jinhui
 * @Date 2022/10/16 9:59
 */
@Slf4j
@Configuration
public class RabbitMQProducerConfig {

    public static final String TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK = "TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK";
    public static final String CHAT_EXCHANGE = "TREE3CHAT_EXCHANGE";
    /**
     * 连接工厂
     */
    @Resource
    private ConnectionFactory connectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息成功发送到了交换机。。{}---,{}---,{}", correlationData, ack, cause);
            } else {
                log.error("消息未能送达到Exchange，需要做后续处理！！！{}", cause);
            }
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息 路由队列失败，后续继续重试：{}", message);
        });
        return rabbitTemplate;
    }
}
