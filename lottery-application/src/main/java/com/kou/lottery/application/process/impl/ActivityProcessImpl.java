package com.kou.lottery.application.process.impl;

import com.kou.lottery.application.mq.producer.RabbitMqProducer;
import com.kou.lottery.application.process.ActivityProcess;
import com.kou.lottery.application.process.req.DrawProcessReq;
import com.kou.lottery.application.process.res.DrawProcessResult;
import com.kou.lottery.application.process.res.RuleQuantificationCrowdResult;
import com.kou.lottery.common.Constants;
import com.kou.lottery.common.Result;
import com.kou.lottery.domain.activity.model.req.PartakeReq;
import com.kou.lottery.domain.activity.model.res.PartakeResult;
import com.kou.lottery.domain.activity.model.vo.ActivityPartakeRecordVO;
import com.kou.lottery.domain.activity.model.vo.DrawOrderVO;
import com.kou.lottery.domain.activity.model.vo.InvoiceVO;
import com.kou.lottery.domain.activity.service.partake.ActivityPartake;
import com.kou.lottery.domain.rule.model.req.DecisionMatterReq;
import com.kou.lottery.domain.rule.model.res.EngineResult;
import com.kou.lottery.domain.rule.service.engine.EngineFilter;
import com.kou.lottery.domain.strategy.model.req.DrawReq;
import com.kou.lottery.domain.strategy.model.res.DrawResult;
import com.kou.lottery.domain.strategy.model.vo.DrawAwardVO;
import com.kou.lottery.domain.strategy.service.draw.DrawExec;
import com.kou.lottery.domain.support.ids.IdGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author MiManchi
 * Date: 2022/6/9 16:00
 * Package: com.kou.lottery.application.process.impl
 */
@Service
public class ActivityProcessImpl implements ActivityProcess {

    @Resource
    private ActivityPartake activityPartake;

    @Resource
    private DrawExec drawExec;

    @Resource(name = "ruleEngineHandle")
    private EngineFilter engineFilter;

    @Resource
    private Map<Constants.Ids, IdGenerator> idGeneratorMap;

    @Resource
    private RabbitMqProducer rabbitMqProducer;

    /**
     * 执行抽奖流程
     * @param req 抽奖请求
     * @return    抽奖结果
     */
    @Override
    public DrawProcessResult doDrawProcess(DrawProcessReq req) {
        //  1.领取活动
        PartakeResult partakeResult = activityPartake.doPartake(new PartakeReq(req.getuId(), req.getActivityId()));
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode())){
            return new DrawProcessResult(partakeResult.getCode(), partakeResult.getInfo());
        }

        // 2. 首次成功领取活动，发送 MQ 消息
        if (Constants.ResponseCode.SUCCESS.getCode().equals(partakeResult.getCode())) {
            ActivityPartakeRecordVO activityPartakeRecord = new ActivityPartakeRecordVO();
            activityPartakeRecord.setuId(req.getuId());
            activityPartakeRecord.setActivityId(req.getActivityId());
            activityPartakeRecord.setStockCount(partakeResult.getStockCount());
            activityPartakeRecord.setStockSurplusCount(partakeResult.getStockSurplusCount());
            // 发送 MQ 消息
            rabbitMqProducer.sendLotteryActivityPartakeRecord(activityPartakeRecord);
        }

        Long strategyId = partakeResult.getStrategyId();
        Long takeId = partakeResult.getTakeId();

        // 3. 执行抽奖
        DrawResult drawResult = drawExec.doDrawExec(new DrawReq(req.getuId(), strategyId));
        if (Constants.DrawState.FAIL.getCode().equals(drawResult.getDrawState())){
            return new DrawProcessResult(Constants.ResponseCode.LOSING_DRAW.getCode(), Constants.ResponseCode.LOSING_DRAW.getInfo());
        }
        DrawAwardVO drawAwardVo = drawResult.getDrawAwardInfo();

        // 4. 结果落库
        DrawOrderVO drawOrderVO = buildDrawOrderVO(req, strategyId, takeId, drawAwardVo);
        Result recordResult = activityPartake.recordDrawOrder(drawOrderVO);
        if (!Constants.ResponseCode.SUCCESS.getCode().equals(recordResult.getCode())) {
            return new DrawProcessResult(recordResult.getCode(), recordResult.getInfo());
        }

        // 5. 发送MQ，触发发奖流程
        InvoiceVO invoiceVO = buildInvoiceVO(drawOrderVO);
        rabbitMqProducer.sendLotteryInvoice(invoiceVO);

        // 6. 返回结果
        return new DrawProcessResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), drawAwardVo);
    }


    /**
     * 规则量化人群，返回可参与的活动ID
     * @param req   规则请求
     * @return      量化结果，用户可以参与的活动ID
     */
    @Override
    public RuleQuantificationCrowdResult doRuleQuantificationCrowd(DecisionMatterReq req) {
        //  1.量化决策
        EngineResult engineResult = engineFilter.process(req);

        if (!engineResult.isSuccess()){
            return new RuleQuantificationCrowdResult(Constants.ResponseCode.RULE_ERR.getCode(),Constants.ResponseCode.RULE_ERR.getInfo());
        }

        //  2.封装结果
        RuleQuantificationCrowdResult ruleQuantificationCrowdResult = new RuleQuantificationCrowdResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo());
        ruleQuantificationCrowdResult.setActivityId(Long.valueOf(engineResult.getNodeValue()));

        return ruleQuantificationCrowdResult;
    }


    private DrawOrderVO buildDrawOrderVO(DrawProcessReq req, Long strategyId, Long takeId, DrawAwardVO drawAwardVO) {
        long orderId = idGeneratorMap.get(Constants.Ids.SnowFlake).nextId();
        DrawOrderVO drawOrderVO = new DrawOrderVO();
        drawOrderVO.setuId(req.getuId());
        drawOrderVO.setTakeId(takeId);
        drawOrderVO.setActivityId(req.getActivityId());
        drawOrderVO.setOrderId(orderId);
        drawOrderVO.setStrategyId(strategyId);
        drawOrderVO.setStrategyMode(drawAwardVO.getStrategyMode());
        drawOrderVO.setGrantType(drawAwardVO.getGrantType());
        drawOrderVO.setGrantDate(drawAwardVO.getGrantDate());
        drawOrderVO.setGrantState(Constants.GrantState.INIT.getCode());
        drawOrderVO.setAwardId(drawAwardVO.getAwardId());
        drawOrderVO.setAwardType(drawAwardVO.getAwardType());
        drawOrderVO.setAwardName(drawAwardVO.getAwardName());
        drawOrderVO.setAwardContent(drawAwardVO.getAwardContent());
        return drawOrderVO;
    }

    private InvoiceVO buildInvoiceVO(DrawOrderVO drawOrderVO) {
        InvoiceVO invoiceVO = new InvoiceVO();
        invoiceVO.setuId(drawOrderVO.getuId());
        invoiceVO.setOrderId(drawOrderVO.getOrderId());
        invoiceVO.setAwardId(drawOrderVO.getAwardId());
        invoiceVO.setAwardType(drawOrderVO.getAwardType());
        invoiceVO.setAwardName(drawOrderVO.getAwardName());
        invoiceVO.setAwardContent(drawOrderVO.getAwardContent());
        invoiceVO.setShippingAddress(null);
        invoiceVO.setExtInfo(null);
        return invoiceVO;
    }
}
