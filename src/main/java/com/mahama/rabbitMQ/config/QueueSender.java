package com.mahama.rabbitMQ.config;

import com.mahama.rabbitMQ.enumeration.QueueExchangeList;
import com.mahama.rabbitMQ.enumeration.QueueFactory;
import com.mahama.rabbitMQ.enumeration.QueueList_Rabbit;
import com.mahama.rabbitMQ.enumeration.QueueRoutingKeyList;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration("QueueSender_MQ")
public class QueueSender {
    /**
     * 连接工厂
     */
    @Bean(QueueFactory.PREFETCH_COUNT_FACTORY_50)
    public SimpleRabbitListenerContainerFactory PREFETCH_COUNT_FACTORY_50(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setPrefetchCount(50);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * 默认交换器
     */
    @Bean(QueueExchangeList.DEFAULT_EXCHANGE)
    public TopicExchange defaultExchange() {
        //创建一个topic交换器
        return new TopicExchange(QueueExchangeList.DEFAULT_EXCHANGE);
    }

    /**
     * 延时交换机
     */
    @Bean(QueueExchangeList.DELAY_EXCHANGE)
    public DirectExchange delayExchange() {
        return new DirectExchange(QueueExchangeList.DELAY_EXCHANGE);
    }

    /**
     * 死信交换机
     */
    @Bean(QueueExchangeList.DEAD_LETTER_EXCHANGE)
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(QueueExchangeList.DEAD_LETTER_EXCHANGE);
    }

    /**
     * 延时队列并绑定到对应的死信交换机
     */
    @Bean(QueueList_Rabbit.DEAD_DELAY)
    public Queue deadDelay() {
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", QueueExchangeList.DEAD_LETTER_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", QueueRoutingKeyList.DEAD_LETTER_10_S);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QueueList_Rabbit.DEAD_DELAY).withArguments(args).build();
    }

    /**
     * 声明10s死信队列 用于接收延时处理的消息
     */
    @Bean(QueueList_Rabbit.DEAD_LETTER_10_S)
    public Queue deadLetter_10s() {
        return new Queue(QueueList_Rabbit.DEAD_LETTER_10_S);
    }

    /**
     * 声明延时队列绑定关系
     */
    @Bean
    public Binding delayBindingDeadDelay(@Qualifier(QueueList_Rabbit.DEAD_DELAY) Queue queue,
                                          @Qualifier(QueueExchangeList.DELAY_EXCHANGE) DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QueueRoutingKeyList.DEAD_DELAY);
    }
}
