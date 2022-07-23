package com.kou.lottery.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author MiManchi
 * Date: 2022/6/4 15:25
 * Package: com.kou.lottery.domain.strategy.model.vo
 *
 * 策略明细简要信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyDetailBriefVO {

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 奖品ID
     */
    private String awardId;

    /**
     * 奖品名称
     */
    private String awardName;

    /**
     * 奖品库存
     */
    private Integer awardCount;

    /**
     * 奖品剩余库存
     */
    private Integer awardSurplusCount;

    /**
     * 中奖概率
     */
    private BigDecimal awardRate;
}
