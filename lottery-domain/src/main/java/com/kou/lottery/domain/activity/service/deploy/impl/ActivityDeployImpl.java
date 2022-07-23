package com.kou.lottery.domain.activity.service.deploy.impl;

import com.alibaba.fastjson.JSON;
import com.kou.lottery.domain.activity.model.aggregates.ActivityConfigRich;
import com.kou.lottery.domain.activity.model.req.ActivityConfigReq;
import com.kou.lottery.domain.activity.model.vo.ActivityVO;
import com.kou.lottery.domain.activity.model.vo.AwardVO;
import com.kou.lottery.domain.activity.model.vo.StrategyDetailVO;
import com.kou.lottery.domain.activity.model.vo.StrategyVO;
import com.kou.lottery.domain.activity.repository.ActivityRepository;
import com.kou.lottery.domain.activity.service.deploy.ActivityDeploy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/5 15:00
 * Package: com.kou.lottery.domain.activity.service.deploy.impl
 */
@Service
@Slf4j
public class ActivityDeployImpl implements ActivityDeploy {

    @Resource
    private ActivityRepository activityRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createActivity(ActivityConfigReq req) {
        log.info("创建活动配置开始，activityId：{}", req.getActivityId());

        ActivityConfigRich activityConfigRich = req.getActivityConfigRich();
        try {
            ActivityVO activity = activityConfigRich.getActivity();
            activityRepository.addActivity(activity);

            List<AwardVO> awardList = activityConfigRich.getAwardList();
            activityRepository.addAward(awardList);

            StrategyVO strategy = activityConfigRich.getStrategy();
            activityRepository.addStrategy(strategy);

            List<StrategyDetailVO> strategyDetailList = activityConfigRich.getStrategy().getStrategyDetailList();
            activityRepository.addStrategyDetailList(strategyDetailList);

            log.info("创建活动配置完成，activityId：{}", req.getActivityId());
        }catch (DuplicateKeyException e){
            log.info("创建活动配置失败，唯一索引冲突 activityId：{} reqJson：{}", req.getActivityId(), JSON.toJSONString(req), e);
            throw e;
        }
    }

    @Override
    public void updateActivity(ActivityConfigReq req) {}

    @Override
    public List<ActivityVO> scanToDoActivityList(Long id) {
        return activityRepository.scanToDoActivityList(id);
    }
}
