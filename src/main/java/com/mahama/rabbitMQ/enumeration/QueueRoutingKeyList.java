package com.mahama.rabbitMQ.enumeration;

/**
 * 路由列表
 */
public interface QueueRoutingKeyList {
    String DEAD_DELAY ="dead.delay";
    String DEAD_LETTER_10_S ="dead.letter.10s";
    String DEAD_LETTER_30_S ="dead.letter.30s";
    String DEAD_LETTER_1_M ="dead.letter.1m";
    String DEAD_LETTER_10_M ="dead.letter.10m";
    String DEAD_LETTER_30_M ="dead.letter.30m";
    String DEAD_LETTER_1_H ="dead.letter.1h";
    String DEAD_LETTER_1_D ="dead.letter.1d";
}
