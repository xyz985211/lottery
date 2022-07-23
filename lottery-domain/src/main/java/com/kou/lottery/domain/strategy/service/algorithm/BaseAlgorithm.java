package com.kou.lottery.domain.strategy.service.algorithm;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.strategy.model.vo.AwardRateVO;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MiManchi
 * Date: 2022/5/27 19:56
 * Package: com.kou.lottery.domain.strategy.service.algorithm
 */
public abstract class BaseAlgorithm implements DrawAlgorithm{

    /**
     * 斐波那契散列增量，逻辑：黄金分割点：(√5 - 1) / 2 = 0.6180339887，Math.pow(2, 32) * 0.6180339887 = 0x61c88647
     */
    private final int HASH_INCREMENT = 0x61c88647;

    /**
     * 数组初始化长度，保证数据填充时不发生碰撞的最小初始化值
     */
    private final int RATE_TUPLE_LENGTH = 128;

    /**
     * 存放概率与奖品对应的散列结果，strategyId -> rateTuple
     */
    protected Map<Long, String[]> rateTupleMap = new ConcurrentHashMap<>();

    /**
     * 奖品区间概率值，strategyId -> [awardId->begin、awardId->end]
     */
    protected Map<Long, List<AwardRateVO>> awardRateInfoMap = new ConcurrentHashMap<>();

    /**
     * 初始化概率元组
     * @param strategyId 策略ID
     * @param awardRateVOList 奖品概率配置集合 「值示例：AwardRateInfo.awardRate = 0.04」
     */
    @Override
    public synchronized void initRateTuple(Long strategyId, Integer strategyMode, List<AwardRateVO> awardRateVOList) {
        // 前置判断
        if (isExist(strategyId)) {
            return;
        }

        // 保存奖品概率信息
        awardRateInfoMap.put(strategyId, awardRateVOList);

        // 非单项概率，不必存入缓存，因为这部分抽奖算法需要实时处理中奖概率。
        if (!Constants.StrategyMode.SINGLE.getCode().equals(strategyMode)) {
            return;
        }

        // computeIfAbsent：如果 key 对应的 value 不存在，则使用获取 remappingFunction 重新计算后的值，并保存为该 key 的 value，否则返回 value。
        String[] rateTuple = rateTupleMap.computeIfAbsent(strategyId, k -> new String[RATE_TUPLE_LENGTH]);

        int cursorVal = 0;
        for (AwardRateVO awardRateVo : awardRateVOList) {
            int rateVal = awardRateVo.getAwardRate().multiply(new BigDecimal(100)).intValue();

            // 循环填充概率范围值
            for (int i = cursorVal + 1; i <= (rateVal + cursorVal); i++) {
                rateTuple[hashIdx(i)] = awardRateVo.getAwardId();
            }

            cursorVal += rateVal;

        }
    }

    /**
     * 判断是否已经，做了数据初始化
     * @param strategyId 策略ID
     * @return 是否做了初始化
     */
    @Override
    public boolean isExist(Long strategyId) {
        return awardRateInfoMap.containsKey(strategyId);
    }

    /**
     * 斐波那契（Fibonacci）散列法，计算哈希索引下标值
     *
     * @param val 值
     * @return 索引
     */
    protected int hashIdx(int val){
        int hashCode = val * HASH_INCREMENT + HASH_INCREMENT;
        return hashCode & (RATE_TUPLE_LENGTH - 1);
    }

    /**
     * 生成百位随机抽奖码
     *
     * @return 随机值
     */
    protected int generateSecureRandomIntCode(int bound){
        return new SecureRandom().nextInt(bound) + 1;
    }
}
