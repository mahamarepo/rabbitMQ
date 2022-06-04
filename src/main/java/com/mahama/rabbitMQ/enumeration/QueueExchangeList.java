package com.mahama.rabbitMQ.enumeration;

/**
 * 交换机列表
 */
public interface QueueExchangeList {
    /**
     * 默认交换机
     */
    String DEFAULT_EXCHANGE = "default.exchange";
    /**
     * 延时交换机
     */
    String DELAY_EXCHANGE = "delay.exchange";
    /**
     * 死信交换机
     */
    String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
}
