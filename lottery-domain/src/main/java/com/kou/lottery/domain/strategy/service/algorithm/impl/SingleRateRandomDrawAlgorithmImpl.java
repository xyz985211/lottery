package com.kou.lottery.domain.strategy.service.algorithm.impl;

import com.kou.lottery.domain.strategy.service.algorithm.BaseAlgorithm;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/5/27 21:04
 * Package: com.kou.lottery.domain.strategy.repository.impl
 *
 * 【推荐】单项随机概率抽奖，抽到一个已经排掉的奖品则未中奖
 */
@Component("singleRateRandomDrawAlgorithmImpl")
public class SingleRateRandomDrawAlgorithmImpl extends BaseAlgorithm {

    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {

        //  获取策略对应的元祖
        String[] rateTuple = super.rateTupleMap.get(strategyId);
        //  断言，相当于if语句，成立就继续往下运行，不成立就终止
        assert rateTuple != null;

        //  随机索引
        int randomVal = this.generateSecureRandomIntCode(100);
        int idx = super.hashIdx(randomVal);

        //  返回结果
        String awardId = rateTuple[idx];
        if (excludeAwardIds.contains(awardId)){
            return null;
        }

        return awardId;
    }
}
