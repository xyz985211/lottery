package com.kou.lottery.application.mq.producer;

import com.alibaba.fastjson.JSON;
import com.kou.lottery.application.mq.config.RabbitMqConfig;
import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.kou.lottery.domain.activity.model.vo.InvoiceVO;
import com.kou.lottery.domain.activity.service.partake.ActivityPartake;
import com.kou.lottery.domain.support.ids.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author MiManchi
 * Date: 2022/6/19 9:03
 * Package: com.kou.lottery.application.mq
 *
 * 消息生产者
 */
@Component
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RabbitMqProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ActivityPartake activityPartake;

    @Resource
    private Map<Constants.Ids, IdGenerator> idGeneratorMap;

    /**
     * 发送中奖物品发货单消息
     *
     * @param invoice 发货单
     */
    public void sendLotteryInvoice(InvoiceVO invoice) {
        init(invoice);

        CorrelationData correlationData = new CorrelationData(idGeneratorMap.get(Constants.Ids.RandomNumeric).nextId() + "");
        String objJson = JSON.toJSONString(invoice);

        rabbitTemplate.convertAndSend(RabbitMqConfig.LOTTERY_INVOICE_EXCHANGE, RabbitMqConfig.LOTTERY_INVOICE_ROUTING_KEY, objJson, correlationData);
        log.info("发送MQ消息(发送中奖物品发货单消息) exchange：{} bizId：{} message：{}", RabbitMqConfig.LOTTERY_INVOICE_EXCHANGE, invoice.getuId(), objJson);
    }

    /**
     * 发送领取活动记录MQ
     *
     * @param activityPartakeRecord 领取活动记录
     */
    public void sendLotteryActivityPartakeRecord(ActivityPartakeRecordVO activityPartakeRecord) {
        String objJson = JSON.toJSONString(activityPartakeRecord);

        CorrelationData correlationData = new CorrelationData(idGeneratorMap.get(Constants.Ids.RandomNumeric).nextId() + "");
        rabbitTemplate.convertAndSend(RabbitMqConfig.LOTTERY_ACTIVITY_PARTAKE_EXCHANGE, RabbitMqConfig.LOTTERY_ACTIVITY_PARTAKE_ROUTING_KEY, objJson, correlationData);

        log.info("发送MQ消息(领取活动记录) exchange：{} bizId：{} message：{}", RabbitMqConfig.LOTTERY_ACTIVITY_PARTAKE_EXCHANGE, activityPartakeRecord.getuId(), objJson);
    }

    private void init(InvoiceVO invoiceVO){
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            //  接收消息
            if(ack){
                // 5.1 MQ 消息发送完成，更新数据库表 user_strategy_export.mq_state = 1
                activityPartake.updateInvoiceMqState(invoiceVO.getuId(), invoiceVO.getOrderId(), Constants.MQState.COMPLETE.getCode());
            }else {
                // 5.2 MQ 消息发送失败，更新数据库表 user_strategy_export.mq_state = 2 【等待定时任务扫码补偿MQ消息】
                activityPartake.updateInvoiceMqState(invoiceVO.getuId(), invoiceVO.getOrderId(), Constants.MQState.FAIL.getCode());
            }
        });

        rabbitTemplate.setReturnsCallback(returnMessage -> {
            log.error("消息{}，被交换机{}退回，退回原因{}，路由key：{}",
                    new String(returnMessage.getMessage().getBody()).getBytes(StandardCharsets.UTF_8),
                    returnMessage.getExchange(), returnMessage.getMessage(),returnMessage.getRoutingKey());
        });
    }
}
