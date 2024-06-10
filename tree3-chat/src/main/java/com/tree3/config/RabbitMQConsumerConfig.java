package com.tree3.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
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


    /**
     * 注入默认的连接工厂
     */
    @Resource
    private ConnectionFactory connectionFactory;


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
     * 配置消费者 消息重试
     * 修改消息失败策略
     * 默认配置： {@link AbstractRabbitListenerContainerFactoryConfigurer#configure(AbstractRabbitListenerContainerFactory, ConnectionFactory, RabbitProperties.AmqpContainer)}
     * MessageRecoverer recoverer = this.messageRecoverer != null ? this.messageRecoverer : new RejectAndDontRequeueRecoverer(); 默认拒绝&不重新排队
     *
     * @param rabbitTemplate
     * @return
     */
    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        /**
         return new RejectAndDontRequeueRecoverer(); // 拒绝&不重新排队(默认)
         return new MessageBatchRecoverer() {public void recover(List<Message> messages, Throwable cause) {}}; // 用于消息批量处理的恢复器（Recoverer），它可以在消息消费失败时对一个批量的消息进行统一的处理。
         return new ImmediateRequeueMessageRecoverer(); // 重新排队 -- 重试之后，返回队列，然后再重试，周而复始直到不抛出异常为止，这样还是会影响后续的消息消费...
         return new RepublishMessageRecoverer(rabbitTemplate, RETRY_EXCHANGE, RETRY_FAILURE_KEY); // 重新发布 -- 重试之后，将消息转发到重试失败队列，由重试失败消费者消费...
         */
        return new RepublishMessageRecoverer(
                rabbitTemplate, RabbitMQElements.TREE3CHAT_EXCHANGE_RETRY, RabbitMQElements.TREE3CHAT_ROUTER$KEY_RETRY$FAIL$PATH
        );
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

    /**
     * <p>
     * 本文件用于注册、管理 RabbitMQ中的各种队列、交换机及两者的绑定关系
     * </p>
     * <a>@Author: Rupert</ a>
     * <p>创建时间: 2024/5/15 9:18 </p>
     */
    @Configuration
    public class RabbitMQElements {
        //region Step 0: 定义各元素的名称
        //项目名称_元素类型_作用_工作模式

        //项目名称_元素类型_作用_工作模式
        public static final String TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK = "TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK";
        //
        public static final String TREE3CHAT_QUEUE_OFFLINE$MESSAGE_WORK = "TREE3CHAT_QUEUE_OFFLINE$MESSAGE_WORK";

        public static final String TREE3CHAT_EXCHANGE_RETRY = "TREE3CHAT_EXCHANGE_RETRY";
        public static final String TREE3CHAT_ROUTER$KEY_RETRY$FAIL$PATH = "TREE3CHAT_ROUTER$KEY_RETRY$FAIL$PATH";
        public static final String TREE3CHAT_QUEUE_RETRY$FAIL = "TREE3CHAT_QUEUE_RETRY$FAIL";
        //endregion Step 0: 定义各元素的名称
        //region Step 1: 创建队列

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
         * 离线消息： 本队列中的消息应当使用手动签收模式进行消费 ;消息的过期时间（2天），过期后的消息,直接存储到数据库
         * unsure：抉择离线消息的存储使用redis还是rabbitmq (Rupert，2024/6/10 10:44)
         *  优劣分析：
         *  redis：存取方便，但是redis是基于内存的缓存，将使用情况未确定的，离线消息存储在内存中。。。。太浪费内存了
         *  RabbitMQ：将离线消息存储在mq中，存取效率相对redis较低（每次都需要遍历所有的消息，如果收件人一致才确认接收消息），否则的不签收消息将消息存回队列
         *
         * @return
         */
        @Bean
        public Queue tree3chatOffLineQueue() {
            return QueueBuilder.durable(TREE3CHAT_QUEUE_OFFLINE$MESSAGE_WORK).build();
        }

        /**
         * 消息重试队列
         *
         * @return
         */
//        @Bean
//        public Queue tree3chatQueueRetryFail() {
//            return QueueBuilder.durable(TREE3CHAT_QUEUE_RETRY$FAIL).build();
//        }

        //endregion Step 1: 创建队列
        //region Step 2: 创建交换机
//        /**
//         * 重试交换机
//         *
//         * @return
//         */
//        @Bean(TREE3CHAT_EXCHANGE_RETRY)
//        public Exchange dlxExchange() {
//            Exchange exchange = ExchangeBuilder
//                    .directExchange(TREE3CHAT_EXCHANGE_RETRY)
//                    .durable(true)
//                    .build();
//            return exchange;
//        }
        //endregion Step 2: 创建交换机
        //region Step 3: 建立队列和交换机间的关系(binding)
//        /**
//         * 死信队列
//         *
//         * @param queue
//         * @param exchange
//         * @return
//         */
//        @Bean
//        public Binding bindingQueueAndExchange(
//                @Qualifier(QUEUE_NAME) Queue queue,
//                @Qualifier(EXCHANGE_DLX_NAME) Exchange exchange
//        ) {
//            return BindingBuilder
//                    .bind(queue)//哪个队列？
//                    .to(exchange)//绑定到 哪个交换机上？
//                    .with("routerPath")//定制路由
//                    .noargs();
//        }
        //endregion Step 3: 建立队列和交换机间的关系(binding)
    }
}

