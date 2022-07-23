package com.kou.lottery.application.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author MiManchi
 * Date: 2022/6/27 15:42
 * Package: com.kou.lottery.application.mq.config
 */
@Configuration
public class RabbitMqConfig {

    /**
     * 中奖发货单交换机
     */
    public static final String LOTTERY_INVOICE_EXCHANGE = "lottery_invoice_exchange";
    /**
     * 中奖发货单队列
     */
    public static final String LOTTERY_INVOICE_QUEUE = "lottery_invoice_queue";
    /**
     * 中奖发货单RoutingKey
     */
    public static final String LOTTERY_INVOICE_ROUTING_KEY = "lottery_invoice_key";


    /**
     * 活动领取记录交换机
     */
    public static final String LOTTERY_ACTIVITY_PARTAKE_EXCHANGE = "lottery_activity_partake_exchange";
    /**
     * 活动领取记录队列
     */
    public static final String LOTTERY_ACTIVITY_PARTAKE_QUEUE = "lottery_activity_partake_queue";
    /**
     * 活动领取记录RoutingKey
     */
    public static final String LOTTERY_ACTIVITY_PARTAKE_ROUTING_KEY = "lottery_activity_partake_key";

    @Bean
    public DirectExchange confirmInvoiceExchange(){
        return new DirectExchange(LOTTERY_INVOICE_EXCHANGE);
    }

    @Bean
    public Queue confirmInvoiceQueue(){
        return QueueBuilder.durable(LOTTERY_INVOICE_QUEUE).build();
    }

    @Bean
    public Binding invoiceQueueBindingExchange(@Qualifier("confirmInvoiceExchange") DirectExchange confirmInvoiceExchange,
                                               @Qualifier("confirmInvoiceQueue") Queue confirmInvoiceQueue){
        return BindingBuilder.bind(confirmInvoiceQueue).to(confirmInvoiceExchange).with(LOTTERY_INVOICE_ROUTING_KEY);
    }

    @Bean
    public DirectExchange confirmPartakeExchange(){
        return new DirectExchange(LOTTERY_ACTIVITY_PARTAKE_EXCHANGE);
    }

    @Bean
    public Queue confirmPartakeQueue(){
        return QueueBuilder.durable(LOTTERY_ACTIVITY_PARTAKE_QUEUE).build();
    }

    @Bean
    public Binding partakeQueueBindingExchange(@Qualifier("confirmPartakeExchange") DirectExchange confirmPartakeExchange,
                                               @Qualifier("confirmPartakeQueue") Queue confirmPartakeQueue){
        return BindingBuilder.bind(confirmPartakeQueue).to(confirmPartakeExchange).with(LOTTERY_ACTIVITY_PARTAKE_ROUTING_KEY);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //设置消息进入交换机后未被队列接收的消息不被丢弃由broker保存,flase为丢弃
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReceiveTimeout(30000);
        rabbitTemplate.setReplyTimeout(30000);
        return rabbitTemplate;
    }
}
