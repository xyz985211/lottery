package com.kou.lottery.application.worker;

import com.alibaba.fastjson.JSON;
import com.kou.lottery.application.mq.producer.RabbitMqProducer;
import com.kou.lottery.common.Constants;
import com.kou.lottery.common.Result;
import com.kou.lottery.domain.activity.model.vo.ActivityVO;
import com.kou.lottery.domain.activity.model.vo.InvoiceVO;
import com.kou.lottery.domain.activity.service.deploy.ActivityDeploy;
import com.kou.lottery.domain.activity.service.partake.ActivityPartake;
import com.kou.lottery.domain.activity.service.stateflow.StateHandler;
import com.kou.middleware.db.router.strategy.IDBRouterStrategy;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/19 19:11
 * Package: com.kou.lottery.application.worker
 *
 * 抽奖业务，任务配置
 */
//@Component
@Slf4j
public class LotteryXxlJob {

    @Resource
    private ActivityDeploy activityDeploy;

    @Resource
    private StateHandler stateHandler;

    @Resource
    private ActivityPartake activityPartake;

    @Resource
    private IDBRouterStrategy dbRouter;

    @Resource
    private RabbitMqProducer rabbitMqProducer;

    @XxlJob("lotteryActivityStateJobHandler")
    public void lotteryActivityStateJobHandler() throws Exception {
        log.info("扫描活动状态 Begin");

        List<ActivityVO> activityVOList = activityDeploy.scanToDoActivityList(0L);
        if (activityVOList.isEmpty()){
            log.info("扫描活动状态 End 暂无符合需要扫描的活动列表");
            return;
        }

        while (!activityVOList.isEmpty()) {
            for (ActivityVO activityVO : activityVOList) {
                Integer state = activityVO.getState();
                switch (state) {
                    // 活动状态为审核通过，在临近活动开启时间前，审核活动为活动中。在使用活动的时候，需要依照活动状态核时间两个字段进行判断和使用。
                    case 4:
                        Result state4Result = stateHandler.doing(activityVO.getActivityId(), Constants.ActivityState.PASS);
                        log.info("扫描活动状态为活动中 结果：{} activityId：{} activityName：{} creator：{}", JSON.toJSONString(state4Result), activityVO.getActivityId(), activityVO.getActivityName(), activityVO.getCreator());
                        break;
                    // 扫描时间已过期的活动，从活动中状态变更为关闭状态【这里也可以细化为2个任务来处理，也可以把时间判断放到数据库中操作】
                    case 5:
                        if (activityVO.getEndDateTime().before(new Date())){
                            Result state5Result = stateHandler.close(activityVO.getActivityId(), Constants.ActivityState.DOING);
                            log.info("扫描活动状态为关闭 结果：{} activityId：{} activityName：{} creator：{}", JSON.toJSONString(state5Result), activityVO.getActivityId(), activityVO.getActivityName(), activityVO.getCreator());
                        }
                        break;
                    default:
                        break;
                }
            }

            // 获取集合中最后一条记录，继续扫描后面10条记录
            ActivityVO activityVO = activityVOList.get(activityVOList.size() - 1);
            activityVOList = activityDeploy.scanToDoActivityList(activityVO.getId());
        }

        log.info("扫描活动状态 End");
    }

    @XxlJob("lotteryOrderMQStateJobHandler")
    public void lotteryOrderMQStateJobHandler() throws Exception {
        // 验证参数
        String jobParam = XxlJobHelper.getJobParam();
        if (null == jobParam) {
            log.info("扫描用户抽奖奖品发放MQ状态[Table = 2*4] 错误 params is null");
            return;
        }

        // 获取分布式任务配置参数信息 参数配置格式：1,2,3 也可以是指定扫描一个，也可以配置多个库，按照部署的任务集群进行数量配置，均摊分别扫描效率更高
        String[] params = jobParam.split(",");
        log.info("扫描用户抽奖奖品发放MQ状态[Table = 2*4] 开始 params：{}", JSON.toJSONString(params));

        if (params.length == 0) {
            log.info("扫描用户抽奖奖品发放MQ状态[Table = 2*4] 结束 params is null");
            return;
        }

        // 获取分库分表配置下的分表数
        int tbCount = dbRouter.tbCount();

        // 循环获取指定扫描库
        for (String param : params) {
            // 获取当前任务扫描的指定分库
            int dbCount = Integer.parseInt(param);

            // 判断配置指定扫描库数，是否存在
            if (dbCount > dbRouter.dbCount()) {
                log.info("扫描用户抽奖奖品发放MQ状态[Table = 2*4] 结束 dbCount not exist");
                continue;
            }

            // 循环扫描对应表
            for (int i = 0; i < tbCount; i++) {

                // 扫描库表数据
                List<InvoiceVO> invoiceVOList = activityPartake.scanInvoiceMqState(dbCount, i);
                log.info("扫描用户抽奖奖品发放MQ状态[Table = 2*4] 扫描库：{} 扫描表：{} 扫描数：{}", dbCount, i, invoiceVOList.size());

                // 补偿 MQ 消息
                for (InvoiceVO invoiceVO : invoiceVOList) {
                    rabbitMqProducer.sendLotteryInvoice(invoiceVO);
                }
            }

        }

        log.info("扫描用户抽奖奖品发放MQ状态[Table = 2*4] 完成 param：{}", JSON.toJSONString(params));
    }
}
