package com.kou.lottery.domain.award.service.factory;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.award.service.goods.DistributionGoods;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MiManchi
 * Date: 2022/5/29 16:47
 * Package: com.kou.lottery.domain.award.service.factory
 *
 * 各类发奖奖品配置类
 */
public class GoodsConfig {

    /** 奖品发放策略组 */
    protected static Map<Integer, DistributionGoods> goodsMap = new ConcurrentHashMap<>();

    @Resource
    private DistributionGoods couponGoods;

    @Resource
    private DistributionGoods descGoods;

    @Resource
    private DistributionGoods physicalGoods;

    @Resource
    private DistributionGoods redeemCodeGoods;

    @PostConstruct
    public void init(){
        goodsMap.put(Constants.AwardType.DESC.getCode(), descGoods);
        goodsMap.put(Constants.AwardType.RedeemCodeGoods.getCode(), redeemCodeGoods);
        goodsMap.put(Constants.AwardType.CouponGoods.getCode(), couponGoods);
        goodsMap.put(Constants.AwardType.PhysicalGoods.getCode(), physicalGoods);
    }
}
