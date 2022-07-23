package com.kou.lottery.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kou.lottery.infrastructure.po.StrategyDetail;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/5/27 17:05
 * Package: com.kou.lottery.infrastructure.dao
 */
public interface StrategyDetailDao extends BaseMapper<StrategyDetail> {

    //StrategyRich queryStrategyRich(Long strategyId);

    /**
     * 根据策略id查找奖品剩余库存为0的奖品id
     * @param strategyId 策略id
     * @return 奖品id集合
     */
    List<String> queryNoStockStrategyAwardList(Long strategyId);

    /**
     * 扣减库存
     * @param strategyDetail strategyId 策略ID awardId 奖品ID
     * @return 扣减结果
     */
    int deductStock(StrategyDetail strategyDetail);
}
