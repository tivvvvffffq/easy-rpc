package com.nxj.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 单元测试
 */
@SpringBootTest
class UserServiceConsumerTest {

    @Resource
    private UserServiceConsumer userServiceConsumer;

    @Test
    void test1() {
        userServiceConsumer.test();
    }
}