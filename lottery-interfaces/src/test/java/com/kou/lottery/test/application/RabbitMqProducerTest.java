package com.kou.lottery.test.application;

import com.kou.lottery.application.mq.producer.RabbitMqProducer;
import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.activity.model.vo.InvoiceVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author MiManchi
 * Date: 2022/6/19 9:07
 * Package: com.kou.lottery.test.application
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RabbitMqProducerTest {

    @Resource
    private RabbitMqProducer rabbitMqProducer;

    @Test
    public void test_send() throws InterruptedException {
        InvoiceVO invoice = new InvoiceVO();
        invoice.setuId("fustack");
        invoice.setOrderId(1444540456057864192L);
        invoice.setAwardId("3");
        invoice.setAwardType(Constants.AwardType.DESC.getCode());
        invoice.setAwardName("Code");
        invoice.setAwardContent("苹果电脑");
        invoice.setShippingAddress(null);
        invoice.setExtInfo(null);

        rabbitMqProducer.sendLotteryInvoice(invoice);

        while (true){
            Thread.sleep(10000);
        }
    }
}
