package com.kou.lottery.domain.award.service.factory;

import com.kou.lottery.domain.award.service.goods.DistributionGoods;
import org.springframework.stereotype.Service;

/**
 * @author MiManchi
 * Date: 2022/5/29 16:48
 * Package: com.kou.lottery.domain.award.service.factory
 *
 * 配送商品简单工厂，提供获取配送服务
 */
@Service
public class DistributionGoodsFactory extends GoodsConfig{

    public DistributionGoods getDistributionGoodsService(Integer awardType){
        return goodsMap.get(awardType);
    }
}
