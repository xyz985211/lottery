package com.kou.lottery.domain.award.service.goods.impl;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.award.model.req.GoodsReq;
import com.kou.lottery.domain.award.model.res.DistributionRes;
import com.kou.lottery.domain.award.service.goods.DistributionBase;
import com.kou.lottery.domain.award.service.goods.DistributionGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author MiManchi
 * Date: 2022/5/29 16:47
 * Package: com.kou.lottery.domain.award.service.goods.impl
 *
 * 兑换码类商品
 */
@Component
@Slf4j
public class RedeemCodeGoods extends DistributionBase implements DistributionGoods {

    @Override
    public DistributionRes doDistribution(GoodsReq req) {

        // 模拟调用实物发奖
        log.info("模拟调用兑换码 uId：{} awardContent：{}", req.getuId(), req.getAwardContent());

        //  更新用户领奖的过程
        super.updateUserAwardState(req.getuId(), req.getOrderId(), req.getAwardId(), Constants.GrantState.COMPLETE.getCode());

        return new DistributionRes(req.getuId(), Constants.AwardState.SUCCESS.getCode(), Constants.AwardState.SUCCESS.getInfo());
    }
}
