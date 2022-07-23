package com.kou.lottery.domain.strategy.repository;

import com.kou.lottery.domain.strategy.model.aggregates.StrategyRich;
import com.kou.lottery.domain.strategy.model.vo.AwardBriefVO;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/5/27 17:20
 * Package: com.kou.lottery.domain.strategy.repository
 *
 * 策略表仓储服务
 */
public interface StrategyRepository {

    /**
     * 通过策略id查找策略配置，策略明细
     * @param strategyId 策略id
     * @return 策略的聚合对象
     */
    StrategyRich queryStrategyRich(Long strategyId);

    /**
     * 根据奖品id查询奖品信息
     * @param awardId 奖品id
     * @return 奖品信息
     */
    AwardBriefVO queryAwardInfo(String awardId);

    /**
     * 根据策略id查找奖品剩余库存为0的奖品id
     * @param strategyId 策略id
     * @return 奖品id集合
     */
    List<String> queryNoStockStrategyAwardList(Long strategyId);

    /**
     * 扣减库存
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return           扣减结果
     */
    boolean deductStock(Long strategyId, String awardId);
}
