package com.kou.lottery.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.activity.model.req.PartakeReq;
import com.kou.lottery.domain.activity.model.res.StockResult;
import com.kou.lottery.domain.activity.model.vo.*;
import com.kou.lottery.domain.activity.repository.ActivityRepository;
import com.kou.lottery.infrastructure.dao.*;
import com.kou.lottery.infrastructure.po.*;
import com.kou.lottery.infrastructure.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/4 16:09
 * Package: com.kou.lottery.infrastructure.repository
 */
@Repository
@Slf4j
public class ActivityRepositoryImpl implements ActivityRepository {

    @Resource
    private ActivityDao activityDao;
    @Resource
    private AwardDao awardDao;
    @Resource
    private StrategyDao strategyDao;
    @Resource
    private StrategyDetailDao strategyDetailDao;
    @Resource
    private UserTakeActivityCountDao userTakeActivityCountDao;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 添加活动配置
     * @param activity 活动配置
     */
    @Override
    public void addActivity(ActivityVO activity) {
        Activity req = new Activity();
        BeanUtils.copyProperties(activity, req);
        activityDao.insert(req);
    }

    /**
     * 添加奖品配置集合
     *
     * @param awardList 奖品配置集合
     */
    @Override
    public void addAward(List<AwardVO> awardList) {
        for (AwardVO awardVO : awardList) {
            Award award = new Award();
            BeanUtils.copyProperties(awardVO, award);
            awardDao.insert(award);
        }
    }

    /**
     * 添加策略配置
     *
     * @param strategy 策略配置
     */
    @Override
    public void addStrategy(StrategyVO strategy) {
        Strategy req = new Strategy();
        BeanUtils.copyProperties(strategy, req);
        strategyDao.insert(req);
    }

    /**
     * 添加策略明细配置
     *
     * @param strategyDetailList 策略明细集合
     */
    @Override
    public void addStrategyDetailList(List<StrategyDetailVO> strategyDetailList) {
        for (StrategyDetailVO strategyDetailVO : strategyDetailList) {
            StrategyDetail strategyDetail = new StrategyDetail();
            BeanUtils.copyProperties(strategyDetailVO, strategyDetail);
            strategyDetailDao.insert(strategyDetail);
        }
    }

    /**
     * 变更活动状态
     *
     * @param activityId    活动ID
     * @param beforeState   修改前状态
     * @param afterState    修改后状态
     * @return              更新结果
     */
    @Override
    public boolean alterStatus(Long activityId, Enum<Constants.ActivityState> beforeState, Enum<Constants.ActivityState> afterState) {
        AlterStateVO alterStateVO = new AlterStateVO(activityId,((Constants.ActivityState) beforeState).getCode(),((Constants.ActivityState) afterState).getCode());
        int count = activityDao.alterState(alterStateVO);
        return 1 == count;
    }

    /**
     * 查询活动账单信息【库存、状态、日期、个人参与次数】
     * @param req 参与活动请求
     * @return    活动账单
     */
    @Override
    public ActivityBillVO queryActivityBill(PartakeReq req) {
        // 查询活动信息
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getActivityId, req.getActivityId());
        Activity activity = activityDao.selectOne(queryWrapper);

        // 从缓存中获取库存
        Object usedStockCountObj =  redisUtil.get(Constants.RedisKey.KEY_LOTTERY_ACTIVITY_STOCK_COUNT(req.getActivityId()));

        // 查询领取次数
        UserTakeActivityCount userTakeActivityCountReq = new UserTakeActivityCount();
        userTakeActivityCountReq.setuId(req.getuId());
        userTakeActivityCountReq.setActivityId(req.getActivityId());
        UserTakeActivityCount userTakeActivityCount = userTakeActivityCountDao.queryUserTakeActivityCount(userTakeActivityCountReq);

        // 封装结果信息
        ActivityBillVO activityBillVO = new ActivityBillVO();
        activityBillVO.setuId(req.getuId());
        activityBillVO.setActivityId(req.getActivityId());
        activityBillVO.setActivityName(activity.getActivityName());
        activityBillVO.setBeginDateTime(activity.getBeginDateTime());
        activityBillVO.setEndDateTime(activity.getEndDateTime());
        activityBillVO.setTakeCount(activity.getTakeCount());
        activityBillVO.setStockCount(activity.getStockCount());
        activityBillVO.setStockSurplusCount(null == usedStockCountObj ? activity.getStockSurplusCount() : activity.getStockCount() - Integer.parseInt(String.valueOf(usedStockCountObj)));
        activityBillVO.setStrategyId(activity.getStrategyId());
        activityBillVO.setState(activity.getState());
        activityBillVO.setUserTakeLeftCount(null == userTakeActivityCount ? null : userTakeActivityCount.getLeftCount());

        return activityBillVO;
    }

    /**
     * 扣减活动库存
     * @param activityId   活动ID
     * @return      扣减结果
     */
    @Override
    public int subtractionActivityStock(Long activityId) {
        return activityDao.subtractionActivityStock(activityId);
    }

    @Override
    public List<ActivityVO> scanToDoActivityList(Long id) {
        List<Activity> activityList = activityDao.scanToDoActivityList(id);
        List<ActivityVO> activityVOList = new ArrayList<>(activityList.size());
        for (Activity activity : activityList) {
            ActivityVO activityVO = new ActivityVO();
            activityVO.setId(activity.getId());
            activityVO.setActivityId(activity.getActivityId());
            activityVO.setActivityName(activity.getActivityName());
            activityVO.setBeginDateTime(activity.getBeginDateTime());
            activityVO.setEndDateTime(activity.getEndDateTime());
            activityVO.setState(activity.getState());
            activityVOList.add(activityVO);
        }
        return activityVOList;
    }

    @Override
    public StockResult subtractionActivityStockByRedis(String uId, Long activityId, Integer stockCount) {
        //  1. 获取抽奖活动库存 Key
        String stockKey = Constants.RedisKey.KEY_LOTTERY_ACTIVITY_STOCK_COUNT(activityId);

        // 2. 扣减库存，目前占用库存数
        Integer stockUsedCount = (int) redisUtil.incr(stockKey, 1);

        // 3. 超出库存判断，进行恢复原始库存
        if (stockUsedCount > stockCount) {
            redisUtil.decr(stockKey, 1);
            return new StockResult(Constants.ResponseCode.OUT_OF_STOCK.getCode(), Constants.ResponseCode.OUT_OF_STOCK.getInfo());
        }

        // 4. 以活动库存占用编号，生成对应加锁Key，细化锁的颗粒度
        String stockTokenKey = Constants.RedisKey.KEY_LOTTERY_ACTIVITY_STOCK_COUNT_TOKEN(activityId, stockUsedCount);

        // 5. 使用 Redis.setNx 加一个分布式锁
        boolean lockToken = redisUtil.setNx(stockTokenKey, 350L);
        if (!lockToken) {
            log.info("抽奖活动{}用户秒杀{}扣减库存，分布式锁失败：{}", activityId, uId, stockTokenKey);
            return new StockResult(Constants.ResponseCode.ERR_TOKEN.getCode(), Constants.ResponseCode.ERR_TOKEN.getInfo());
        }

        return new StockResult(Constants.ResponseCode.SUCCESS.getCode(), Constants.ResponseCode.SUCCESS.getInfo(), stockTokenKey, stockCount - stockUsedCount);
    }

    @Override
    public void recoverActivityCacheStockByRedis(Long activityId, String tokenKey, String code) {
        // 删除分布式锁 Key
        redisUtil.del(tokenKey);
    }
}
