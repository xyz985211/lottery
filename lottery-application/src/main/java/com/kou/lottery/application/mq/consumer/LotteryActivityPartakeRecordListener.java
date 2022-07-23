package com.kou.lottery.application.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.kou.lottery.application.mq.config.RabbitMqConfig;
import com.kou.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.kou.lottery.domain.activity.service.partake.ActivityPartake;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author MiManchi
 * Date: 2022/6/20 9:37
 * Package: com.kou.lottery.application.mq.consumer
 *
 * 抽奖活动领取记录监听消息
 */
@Component
@Slf4j
public class LotteryActivityPartakeRecordListener {

    @Resource
    private ActivityPartake activityPartake;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConfig.LOTTERY_ACTIVITY_PARTAKE_QUEUE),
            exchange = @Exchange(value = RabbitMqConfig.LOTTERY_ACTIVITY_PARTAKE_EXCHANGE),
            key = RabbitMqConfig.LOTTERY_ACTIVITY_PARTAKE_ROUTING_KEY
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) {

        try {
            // 1. 转化对象
            ActivityPartakeRecordVO activityPartakeRecordVO = JSON.parseObject(new String(message.getBody()).getBytes(StandardCharsets.UTF_8) , ActivityPartakeRecordVO.class);
            log.info("消费MQ消息，异步扣减活动库存 message：{}", JSON.toJSONString(activityPartakeRecordVO));

            // 2. 更新数据库库存【实际场景业务体量较大，可能也会由于MQ消费引起并发，对数据库产生压力，所以如果并发量较大，可以把库存记录缓存中，并使用定时任务进行处理缓存和数据库库存同步，减少对数据库的操作次数】
            activityPartake.updateActivityStock(activityPartakeRecordVO);

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
