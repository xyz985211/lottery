package com.kou.lottery.domain.strategy.service.algorithm.impl;

import com.kou.lottery.domain.strategy.model.vo.AwardRateVO;
import com.kou.lottery.domain.strategy.service.algorithm.BaseAlgorithm;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/5/27 20:30
 * Package: com.kou.lottery.domain.strategy.service.algorithm.impl
 *
 * 必中奖策略抽奖，将已经中奖的概率排除，重新计算中奖范围
 */
@Component("entiretyRateRandomDrawAlgorithmImpl")
public class EntiretyRateRandomDrawAlgorithmImpl extends BaseAlgorithm {

    /**
     * SecureRandom 生成随机数，索引到对应的奖品信息返回结果
     * @param strategyId 策略ID
     * @param excludeAwardIds 排除掉已经不能作为抽奖的奖品ID，留给风控和空库存使用
     * @return 中奖结果
     */
    @Override
    public String randomDraw(Long strategyId, List<String> excludeAwardIds) {

        BigDecimal differenceDenominator = BigDecimal.ZERO;

        // 排除掉不在抽奖范围的奖品ID集合
        List<AwardRateVO> differenceAwardRateList = new ArrayList<>();
        List<AwardRateVO> awardRateIntervalValList = super.awardRateInfoMap.get(strategyId);

        for (AwardRateVO awardRateVo : awardRateIntervalValList) {
            String awardId = awardRateVo.getAwardId();
            if (excludeAwardIds.contains(awardId)){
                continue;
            }
            differenceAwardRateList.add(awardRateVo);
            differenceDenominator = differenceDenominator.add(awardRateVo.getAwardRate());
        }

        /**
         * 前置判断
         * 奖品列表为0，返回NULL
         * 奖品列表为1，直接返回
         */
        if (differenceAwardRateList.size() == 0){
            return null;
        }
        if (differenceAwardRateList.size() == 1){
            return differenceAwardRateList.get(0).getAwardId();
        }

        /**
         * 获取随机概率值
         */
        int randomVal = super.generateSecureRandomIntCode(100);

        /**
         * 循环获取奖品
         */
        String awardId = null;
        int cursorVal = 0;
        for (AwardRateVO awardRateVo : differenceAwardRateList) {
            int rateVal = awardRateVo.getAwardRate()
                    //  返回 BigDecimal ，其值是 (this / divisor) ，其标为 this.scale()
                    .divide(differenceDenominator, 2, BigDecimal.ROUND_UP)
                    //  返回 BigDecimal ，其值是 (this × multiplicand)，其标为 (this.scale() + multiplicand.scale())
                    .multiply(new BigDecimal(100))
                    .intValue();
            if (randomVal <= (cursorVal + rateVal)) {
                awardId = awardRateVo.getAwardId();
                break;
            }
            cursorVal += rateVal;
        }

        return awardId;
    }
}
