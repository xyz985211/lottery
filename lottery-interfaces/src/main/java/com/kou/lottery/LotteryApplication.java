package com.kou.lottery;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author MiManchi
 * Date: 2022/5/25 16:43
 * Package: com.kou.lottery
 */
@SpringBootApplication
@EnableDubbo
@Configurable
@MapperScan(value = "com.kou.lottery.infrastructure.dao")
public class LotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }
}
