package com.kou.lottery.infrastructure.repository;

import com.kou.lottery.domain.award.repository.AwardRepository;
import com.kou.lottery.infrastructure.dao.UserStrategyExportDao;
import com.kou.lottery.infrastructure.po.UserStrategyExport;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author MiManchi
 * Date: 2022/5/29 16:42
 * Package: com.kou.lottery.domain.award.repository.impl
 *
 * 奖品表仓储服务
 */
@Repository
public class AwardRepositoryImpl implements AwardRepository {

    @Resource
    private UserStrategyExportDao userStrategyExportDao;

    @Override
    public void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState) {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        userStrategyExport.setuId(uId);
        userStrategyExport.setOrderId(orderId);
        userStrategyExport.setAwardId(awardId);
        userStrategyExport.setGrantState(grantState);
        userStrategyExportDao.updateUserAwardState(userStrategyExport);
    }
}
