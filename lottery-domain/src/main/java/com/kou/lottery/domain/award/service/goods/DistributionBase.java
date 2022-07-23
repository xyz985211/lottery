package com.kou.lottery.domain.award.service.goods;

import com.kou.lottery.domain.award.repository.AwardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author MiManchi
 * Date: 2022/5/29 16:43
 * Package: com.kou.lottery.domain.award.service.goods
 *
 * 配送货物基础共用类
 */
@Slf4j
public class DistributionBase {

    @Autowired
    private AwardRepository awardRepository;

    protected void updateUserAwardState(String uId, Long orderId, String awardId, Integer grantState){
        awardRepository.updateUserAwardState(uId, orderId, awardId, grantState);
    }
}
