package com.tree3.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @Description
 * @Author: Jinhui
 * @Date 2022/10/16 9:59
 */
@Slf4j
@Configuration
public class RabbitMQConsumerConfig {
    private final static String mqConvertClassName = "className";
    private final static String isArray = "isArray";

    //项目名称_元素类型_作用_工作模式
    public static final String TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK = "TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK";
    //
    public static final String TREE3CHAT_QUEUE_OFFLINE$MESSAGE_WORK = "TREE3CHAT_QUEUE_OFFLINE$MESSAGE_WORK";
    /**
     * 注入默认的连接工厂
     */
    @Resource
    private ConnectionFactory connectionFactory;


    /**
     * 采用工作队列模式完成对聊天记录的 数据库存储
     * （推荐：在 消费端进行 队列的创建）
     * <p>
     * durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     * exclusive 表示该消息队列是否只在当前connection生效,默认是false
     * auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     *
     * @return
     */
    @Bean
    public Queue tree3chatQueue() {
        return QueueBuilder.durable(TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK).build();
    }


    /**
     * 专门用于 暂存离线消息
     * todo 离线消息： 本队列中的消息应当使用手动签收模式进行消费 ;消息的过期时间：一个月起步，过期后的消息,直接存储到数据库
     *
     * @return
     */
    @Bean
    public Queue tree3chatOffLineQueue() {
        return QueueBuilder.durable(TREE3CHAT_QUEUE_OFFLINE$MESSAGE_WORK).build();
    }


    @Bean
    public RabbitTemplate rabbitTemplate() {
        //Step 1: 将连接工厂和template进行绑定（必需）
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //Step 2: 配置消息的序列化机制（可选）——设置message的序列化采用JSON方式，（默认使用jdk序列化）
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        //Step 2: 配置消息的序列化机制
//        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    /**
     * mq消息转换
     *
     * @return
     */
//    @Bean
    public MessageConverter messageConverter() {
        return new AbstractMessageConverter() {
            /**
             * 接收消息时转换
             * @param message
             * @return
             * @throws MessageConversionException
             */
            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                byte[] body = message.getBody();
                MessageProperties messageProperties = message.getMessageProperties();
                //需要转为对应类的类名
                String className = (String) messageProperties.getHeaders().get(mqConvertClassName);
                try {
                    Boolean aBoolean = (Boolean) messageProperties.getHeaders().get(isArray);
                    if (aBoolean != null && aBoolean) {
                        return JSONArray.parseArray(new String(body), Class.forName(className));
                    } else {
                        return JSON.parseObject(body, Class.forName(className));
                    }
                } catch (ClassNotFoundException e) {
                    log.error("mq转换错误: message ==> {} body ==> {}", JSON.toJSONString(message), new String(body), e);
                    throw new RuntimeException(e);
                }
            }

            /**
             * 发送消息时转换
             * @param object
             * @param messageProperties
             * @return
             */
            @Override
            protected Message createMessage(Object object, MessageProperties messageProperties) {
                messageProperties.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                //标记时什么类转换过来的
                messageProperties.setHeader(mqConvertClassName, object.getClass().getName());
                //如果是数组，则设置数组成员的类型（取第一个，只允许类型相同的数组）
                if (object instanceof Collection) {
                    String className;
                    Collection coll = (Collection) object;
                    if (coll.size() > 0) {
                        Object next = coll.iterator().next();
                        className = next.getClass().getName();
                    } else {
                        className = Object.class.getName();
                    }
                    messageProperties.setHeader(mqConvertClassName, className);
                    messageProperties.setHeader(isArray, true);
                } else {
                    messageProperties.setHeader(mqConvertClassName, object.getClass().getName());
                }
                return new Message(JSON.toJSONBytes(object), messageProperties);
            }
        };
    }

}

