package com.kou.lottery.domain.award.repository;

/**
 * @author MiManchi
 * Date: 2022/5/29 16:41
 * Package: com.kou.lottery.domain.award.repository
 *
 * 奖品表仓储服务接口
 */
public interface AwardRepository {

    /**
     * 更新奖品发放状态
     *
     * @param uId               用户ID
     * @param orderId           订单ID
     * @param awardId           奖品ID
     * @param grantState        奖品状态
     */
    void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState);
}
