package com.kou.lottery.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author MiManchi
 * Date: 2022/5/27 17:19
 * Package: com.kou.lottery.domain.strategy.model.vo
 *
 * 奖品概率信息，奖品编号、库存、概率
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwardRateVO {

    /**
     * 奖品ID
     */
    private String awardId;

    /**
     * 中奖概率
     */
    private BigDecimal awardRate;
}
