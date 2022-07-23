package com.kou.lottery.application.mq.consumer;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.kou.lottery.application.mq.config.RabbitMqConfig;
import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.activity.model.vo.InvoiceVO;
import com.kou.lottery.domain.award.model.req.GoodsReq;
import com.kou.lottery.domain.award.model.res.DistributionRes;
import com.kou.lottery.domain.award.service.factory.DistributionGoodsFactory;
import com.kou.lottery.domain.award.service.goods.DistributionGoods;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author MiManchi
 * Date: 2022/6/19 9:02
 * Package: com.kou.lottery.application.mq
 *
 * 消息消费者
 */
@Component
@Slf4j
public class LotteryInvoiceListener {

    @Resource
    private DistributionGoodsFactory distributionGoodsFactory;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConfig.LOTTERY_INVOICE_QUEUE),
            exchange = @Exchange(value = RabbitMqConfig.LOTTERY_INVOICE_EXCHANGE),
            key = RabbitMqConfig.LOTTERY_INVOICE_ROUTING_KEY
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) {

        // 1. 处理 MQ 消息
        try {
            // 1. 转化对象
            InvoiceVO invoiceVO = JSON.parseObject(new String(message.getBody()).getBytes(StandardCharsets.UTF_8), InvoiceVO.class);

            // 2. 获取发送奖品工厂，执行发奖
            DistributionGoods distributionGoodsService = distributionGoodsFactory.getDistributionGoodsService(invoiceVO.getAwardType());
            DistributionRes distributionRes = distributionGoodsService.doDistribution(new GoodsReq(invoiceVO.getuId(), invoiceVO.getOrderId(), invoiceVO.getAwardId(), invoiceVO.getAwardName(), invoiceVO.getAwardContent()));

            Assert.isTrue(Constants.AwardState.SUCCESS.getCode().equals(distributionRes.getCode()), distributionRes.getInfo());

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);

            // 3. 打印日志
            log.info("消费MQ消息，完成发奖环节 exchange：{} bizId：{} 发奖结果：{}", RabbitMqConfig.LOTTERY_INVOICE_EXCHANGE, invoiceVO.getuId(), JSON.toJSONString(distributionRes));
        } catch (Exception e) {
            // 发奖环节失败，消息重试。所有到环节，发货、更新库，都需要保证幂等。
            log.error("消费MQ消息，失败 topic：{} message：{}", RabbitMqConfig.LOTTERY_INVOICE_EXCHANGE, new String(message.getBody()).getBytes(StandardCharsets.UTF_8));
            e.printStackTrace();
        }
    }
}
