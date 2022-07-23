package com.kou.lottery.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MiManchi
 * Date: 2022/6/4 15:24
 * Package: com.kou.lottery.domain.strategy.model.vo
 *
 * 策略简要信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyBriefVO {

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 策略描述
     */
    private String strategyDesc;

    /**
     * 策略方式「1:单项概率、2:总体概率」
     */
    private Integer strategyMode;

    /**
     * 发放奖品方式「1:即时、2:定时[含活动结束]、3:人工」
     */
    private Integer grantType;

    /**
     * 发放奖品时间
     */
    private Date grantDate;

    /**
     * 扩展信息
     */
    private String extInfo;
}
